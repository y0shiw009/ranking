package controllers

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.HBaseConfiguration
import play.api.mvc.Action
import play.api.mvc.Controller
import org.apache.hadoop.hbase.client.HTable
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.client.HBaseAdmin

object Application extends Controller {

    def index = Action {
        println("debug01")
        val conf: Configuration = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "ec2-54-250-225-111.ap-northeast-1.compute.amazonaws.com");
        conf.set("hbase.zookeeper.property.clientPort", "2181");

        println("debug011")

        val table = new HTable(conf, "test")

        println("debug02")
        val result = table.get(new Get(Bytes.toBytes("row")))

        println("debug03")
        val value = result.getColumnLatest(Bytes.toBytes("family1"), Bytes.toBytes("qualifier"))

        println(Bytes.toString(value.getValue()))
        Ok(views.html.index("Your new application is ready."))
    }

}