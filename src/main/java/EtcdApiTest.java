import io.etcd.jetcd.*;
import io.etcd.jetcd.api.WatchGrpc;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.lease.LeaseGrantResponse;
import io.etcd.jetcd.lock.LockResponse;
import io.etcd.jetcd.options.DeleteOption;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.WatchOption;
import io.etcd.jetcd.watch.WatchResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by szf on 2019/4/29.
 */
public class EtcdApiTest {

    public static void main(String[] args) {
        //KV测试能满足我们的需要
        //KVtest();
        //LockTest();
        //KVPathTest();
        subscribeTest();
    }

    public static void KVtest() {
        try {
            Client client = Client.builder().endpoints("http://10.186.60.96:2379").build();
            KV kvClient = client.getKVClient();

            ByteSequence key = ByteSequence.from("/test_key".getBytes());
            ByteSequence value = ByteSequence.from("test_value".getBytes());

            // put the key-value
            kvClient.put(key, value).get();

            // get the CompletableFuture
            CompletableFuture<GetResponse> getFuture = kvClient.get(ByteSequence.from("test_key".getBytes()));

// get the value from CompletableFuture
            GetResponse response = getFuture.get();
            List<KeyValue> kvlist = response.getKvs();
            System.out.println("sdfsdf+   ==" + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 同样是基于renew的lock，问题是订阅怎么办，特别是online的订阅可能存在一些故障
     * online我们实质的需求其实是一个临时节点，在这个地方我们可以进行部分的舍弃
     *
     */
    public static void LockTest() {
        try {
            Client client = Client.builder().endpoints("http://10.186.60.96:2379").build();
            KV kvClient = client.getKVClient();

            Lock lock = client.getLockClient();

            Lease session = client.getLeaseClient();

            CompletableFuture<LeaseGrantResponse> lgr = session.grant(100);
            LeaseGrantResponse sessioxn = lgr.get();

            CompletableFuture<LockResponse> resulto = lock.lock(ByteSequence.from("/lock_test".getBytes()), sessioxn.getID());
            LockResponse rsx = resulto.get();
            System.out.println("lock success" + rsx.getKey());

            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        CompletableFuture<LockResponse> result = lock.lock(ByteSequence.from("/lock_test".getBytes()), sessioxn.getID());
                        LockResponse rs = result.get();
                        System.out.println("lock success2" + result.get().getKey());
                        for (; ; ) {
                            session.keepAliveOnce(sessioxn.getID());
                            Thread.sleep(2000l);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            th.start();
            lock.unlock(ByteSequence.from("/lock_test".getBytes()));
            System.out.println("lock success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void KVPathTest() {
        try {
            Client client = Client.builder().endpoints("http://10.186.60.96:2379").build();
            KV kvClient = client.getKVClient();


            ByteSequence key = ByteSequence.from("/xxxxxx".getBytes());
            ByteSequence key1 = ByteSequence.from("/xxxxxx/vvvv".getBytes());
            ByteSequence key2 = ByteSequence.from("/xxxxxx/zzzz".getBytes());
            ByteSequence value = ByteSequence.from("test_value".getBytes());

            // put the key-value
            kvClient.put(key, value).get();
            kvClient.put(key1, value).get();
            kvClient.put(key2, value).get();

            // get the CompletableFuture
            CompletableFuture<GetResponse> getFuture = kvClient.get(ByteSequence.from("/".getBytes()), GetOption.newBuilder().withPrefix(ByteSequence.from("/".getBytes())).build());
// get the value from CompletableFuture
            GetResponse response = getFuture.get();
            List<KeyValue> kvlist = response.getKvs();
            System.out.println("sdfsdf+   ==" + response);
            kvClient.delete(ByteSequence.from("/xxxxxx".getBytes()), DeleteOption.newBuilder().withPrefix(ByteSequence.from("/".getBytes())).build()).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void subscribeTest() {
        try {
            Client client = Client.builder().endpoints("http://10.186.60.96:2379").build();

            Watch wClient = client.getWatchClient();

            ByteSequence key = ByteSequence.from("/test_key".getBytes());
            Watch.Watcher x = wClient.watch(key, WatchOption.newBuilder().withPrefix(ByteSequence.from("".getBytes())).build(),new Watch.Listener(){

                @Override
                public void onNext(WatchResponse response) {
                    System.out.print("get listener");
                }

                @Override
                public void onError(Throwable throwable) {
                    System.out.print("get listener");
                }

                @Override
                public void onCompleted() {
                    System.out.print("get listener");
                }
            });

            System.out.print("get dddd listener");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
