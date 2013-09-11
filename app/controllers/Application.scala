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

object Application extends Controller {

    val conf = {
        val conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "ec2-54-250-230-72.ap-northeast-1.compute.amazonaws.com");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf
    }

    val connection = HConnectionManager.createConnection(conf);

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

    def index = Action {
        println("debug01")
        val table: HTableInterface = connection.getTable("test");

        println("debug02")
        val result = table.get(new Get(Bytes.toBytes("row")))

        println("debug03")
        val value = result.getColumnLatest(Bytes.toBytes("family1"), Bytes.toBytes("qualifier"))

        println(Bytes.toString(value.getValue()))
        table.close();
        Ok(views.html.index("Your new application is ready."))
    }

}