package controllers

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.client.HConnectionManager
import org.apache.hadoop.hbase.client.HTableInterface
import org.apache.hadoop.hbase.util.Bytes
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.mvc.Result
import org.apache.hadoop.hbase.client.Put

object Application extends Controller {

    val conf = {
        val conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "ec2-54-250-230-72.ap-northeast-1.compute.amazonaws.com");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf
    }

    val connection = HConnectionManager.createConnection(conf);

    def rndget(cid: String, evt: String) = Action {
        val table: HTableInterface = connection.getTable("ranking");
        val rnd = rndAsid
        val result = table.get(new Get(Bytes.toBytes(rnd)))
        val value = result.getColumnLatest(Bytes.toBytes("data"), Bytes.toBytes(s"cid=${cid}~evt=${evt}"))
        val m = Map("asid" -> rnd, "evt" -> Bytes.toString(value.getValue()))
        table.close
        toJsonResult(Ok(Json.toJson(m)))
    }

    def get(asid: String, evt: String) = Action {
        val table: HTableInterface = connection.getTable("ranking");
        val result = table.get(new Get(Bytes.toBytes(asid)))
        val value = result.getColumnLatest(Bytes.toBytes("data"), Bytes.toBytes("cid=gf~evt=g1"))
        val m = Map("asid" -> asid, "evt" -> Bytes.toString(value.getValue()))
        table.close
        toJsonResult(Ok(Json.toJson(m)))
    }

    def inc(asid: String, evt: String, pnt: String) = Action {
        val table: HTableInterface = connection.getTable("ranking");
        val result = table.get(new Get(Bytes.toBytes(asid)))
        val value = result.getColumnLatest(Bytes.toBytes("data"), Bytes.toBytes("cid=gf~evt=g1"))
        val m = Map("asid" -> asid, "evt" -> Bytes.toString(value.getValue()))
        table.close
        toJsonResult(Ok(Json.toJson(m)))
    }

    private def toJsonResult(result: Result): Result = {
        result.withHeaders("Accept" -> "application/json",
            "Content-Type" -> "application/json; charset=utf-8")
    }

    /**
     * 0～200000のasIdを投入する
     */
    def init(cid: String, evt: String, num: String) = Action {
        val table: HTableInterface = connection.getTable("ranking");
        for (i <- 0 to num.toInt) {
            val p = new Put(Bytes.toBytes("%040d".format(i)));
            p.add(Bytes.toBytes("data"), Bytes.toBytes(s"cid=${cid}~evt=${evt}"), Bytes.toBytes(0.toString));
            table.put(p);
        }
        table.close
        Ok("create data")
    }

    /**
     * 0～200000までのランダムの数字を返す。
     * 左パディング、40桁にする
     */
    private def rndAsid: String = {
        //"00383a88ede3e64034ea10e25f7e57ad868f146e"
        val a = scala.math.random
        val d = scala.math.floor(a * 1000000 / 5)
        "%040d".format(d.toInt)
        ""
    }
}