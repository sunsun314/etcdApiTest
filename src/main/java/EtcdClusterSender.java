import com.actiontech.dble.cluster.AbstractClusterSender;
import com.actiontech.dble.cluster.bean.ClusterAlertBean;
import com.actiontech.dble.cluster.bean.KvBean;
import com.actiontech.dble.cluster.bean.SubscribeRequest;
import com.actiontech.dble.cluster.bean.SubscribeReturnBean;

import java.util.List;
import java.util.Properties;

/**
 * Created by szf on 2019/4/29.
 */
public class EtcdClusterSender extends AbstractClusterSender {

    public void initConInfo(Properties properties) {

    }

    public void initCluster(Properties properties) {

    }

    public String lock(String s, String s1) throws Exception {
        return null;
    }

    public void unlockKey(String s, String s1) {

    }

    public void setKV(String s, String s1) throws Exception {

    }

    public KvBean getKV(String s) {
        return null;
    }

    public List<KvBean> getKVPath(String s) {
        return null;
    }

    public void cleanPath(String s) {

    }

    public void cleanKV(String s) {

    }

    public SubscribeReturnBean subscribeKvPrefix(SubscribeRequest subscribeRequest) throws Exception {
        return null;
    }

    public void alert(ClusterAlertBean clusterAlertBean) {

    }

    public boolean alertResolve(ClusterAlertBean clusterAlertBean) {
        return false;
    }

    public void checkClusterConfig(Properties properties) {

    }
}
