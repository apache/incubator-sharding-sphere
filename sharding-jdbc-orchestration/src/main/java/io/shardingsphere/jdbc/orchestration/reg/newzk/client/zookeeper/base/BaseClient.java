/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingsphere.jdbc.orchestration.reg.newzk.client.zookeeper.base;

import io.shardingsphere.jdbc.orchestration.reg.newzk.client.action.IClient;
import io.shardingsphere.jdbc.orchestration.reg.newzk.client.action.IExecStrategy;
import io.shardingsphere.jdbc.orchestration.reg.newzk.client.action.IProvider;
import io.shardingsphere.jdbc.orchestration.reg.newzk.client.utility.PathUtil;
import io.shardingsphere.jdbc.orchestration.reg.newzk.client.utility.StringUtil;
import io.shardingsphere.jdbc.orchestration.reg.newzk.client.utility.Constants;
import io.shardingsphere.jdbc.orchestration.reg.newzk.client.zookeeper.section.ClientContext;
import io.shardingsphere.jdbc.orchestration.reg.newzk.client.zookeeper.section.Listener;
import io.shardingsphere.jdbc.orchestration.reg.newzk.client.zookeeper.section.StrategyType;
import io.shardingsphere.jdbc.orchestration.reg.newzk.client.zookeeper.section.WatcherCreator;
import io.shardingsphere.jdbc.orchestration.reg.newzk.client.zookeeper.strategy.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.data.ACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/*
 * @author lidongbo
 */
public abstract class BaseClient implements IClient {
    private static final Logger logger = LoggerFactory.getLogger(BaseClient.class);
    
    private final int CIRCLE_WAIT = 30;
    protected final boolean watched = true; //false
    protected final Map<StrategyType, IExecStrategy> strategies = new ConcurrentHashMap<>();
    
    protected boolean rootExist = false;
    protected List<ACL> authorities;
    protected Holder holder;
    
    @Getter
    protected IExecStrategy strategy;
    @Getter
    protected BaseContext context;
    
    @Setter
    protected String rootNode = "/InitValue";
    
    protected BaseClient(final BaseContext context) {
        this.context = context;
    }
    
    @Override
    public void start() throws IOException, InterruptedException {
        holder = new Holder(getContext());
        holder.start();
    }
    
    @Override
    public synchronized boolean blockUntilConnected(int wait, TimeUnit units) throws InterruptedException {
        long maxWait = units != null ? TimeUnit.MILLISECONDS.convert(wait, units) : 0;

        while (!holder.isConnected()){
            long waitTime = maxWait - CIRCLE_WAIT;
            if (waitTime <= 0){
                return holder.isConnected();
            }
            wait(CIRCLE_WAIT);
        }
        return true;
    }
    
    @Override
    public void close() {
        this.strategies.clear();
        context.close();
        try {
            this.deleteNamespace();
        } catch (Exception e) {
            logger.error("zk client close delete root error:{}", e.getMessage(), e);
        }
        holder.close();
    }
    
    @Override
    public synchronized void useExecStrategy(StrategyType strategyType) {
        logger.debug("useExecStrategy:{}", strategyType);
        if (strategies.containsKey(strategyType)){
            strategy = strategies.get(strategyType);
            return;
        }
        
        IProvider provider = new BaseProvider(rootNode, holder, watched, authorities);
        switch (strategyType){
            case USUAL:{
                strategy = new UsualStrategy(provider);
                break;
            }
            case CONTEND:{
                strategy = new ContentionStrategy(provider);
                break;
            }
            case SYNC_RETRY:{
                strategy = new SyncRetryStrategy(provider, ((ClientContext)context).getDelayRetryPolicy());
                break;
            }
            case ASYNC_RETRY:{
                strategy = new AsyncRetryStrategy(provider, ((ClientContext)context).getDelayRetryPolicy());
                break;
            }
            default:{
                strategy = new UsualStrategy(provider);
                break;
            }
        }
        
        strategies.put(strategyType, strategy);
    }
    
    void registerWatch(final Listener globalListener){
        if (context.globalListener != null){
            logger.warn("global listener can only register one");
            return;
        }
        context.globalListener = globalListener;
        logger.debug("globalListenerRegistered:{}", globalListener.getKey());
    }
    
    @Override
    public void registerWatch(final String key, final Listener listener){
        String path = PathUtil.getRealPath(rootNode, key);
        listener.setPath(path);
        context.getWatchers().put(listener.getKey(), listener);
        logger.debug("register watcher:{}", path);
    }
    
    @Override
    public void unregisterWatch(final String key){
        if (StringUtil.isNullOrBlank(key)){
            throw new IllegalArgumentException("key should not be blank");
        }
//        String path = PathUtil.getRealPath(rootNode, key);
        if (context.getWatchers().containsKey(key)){
            context.getWatchers().remove(key);
            logger.debug("unregisterWatch:{}", key);
        }
    }
    
    protected void createNamespace() throws KeeperException, InterruptedException {
        createNamespace(Constants.NOTHING_DATA);
    }
    
   private void createNamespace(final byte[] date) throws KeeperException, InterruptedException {
        if (rootExist){
            logger.debug("root exist");
            return;
        }
        try {
            if (null == holder.getZooKeeper().exists(rootNode, false)){
                holder.zooKeeper.create(rootNode, date, authorities, CreateMode.PERSISTENT);
            }
            rootExist = true;
            logger.debug("creating root:{}", rootNode);
        } catch (KeeperException.NodeExistsException ee){
            logger.warn("root create:{}", ee.getMessage());
            rootExist = true;
            return;
        }
        holder.zooKeeper.exists(rootNode, WatcherCreator.deleteWatcher(new Listener(rootNode) {
            @Override
            public void process(WatchedEvent event) {
                rootExist = false;
            }
        }));
        logger.debug("created root:{}", rootNode);
    }
    
    protected void deleteNamespace() throws KeeperException, InterruptedException {
        try {
            holder.zooKeeper.delete(rootNode, Constants.VERSION);
        } catch (KeeperException.NodeExistsException | KeeperException.NotEmptyException ee){
            logger.info("delete root :{}", ee.getMessage());
        }
        rootExist = false;
        logger.debug("delete root:{},rootExist:{}", rootNode, rootExist);
    }
    
    void setAuthorities(final String scheme, final byte[] auth, final List<ACL> authorities) {
        context.scheme = scheme;
        context.auth = auth;
        this.authorities = authorities;
    }
    
    public BaseContext getContext(){
        return context;
    }
    
    public IExecStrategy getStrategy() {
        return strategy;
    }
}
