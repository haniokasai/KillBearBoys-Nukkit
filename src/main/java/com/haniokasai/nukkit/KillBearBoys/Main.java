package com.haniokasai.nukkit.KillBearBoys;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.plugin.PluginBase;


/*
Logs
	13.8.18 1.0 Begin
	13.8.20 1.0.1
	13.8.24 1.0.2
	13.11.16 2.0
		-rewrite all things
	13.11.23 2.0.1
		-Fix bug
	14.3.1 2.0.2
		-Remove default timezone
    14.10.18 2.0.3 and 2.0.4
             -1.4Beta Support(ljy Thank you!)
	15.09.16 3.0.0
		-Database changed to sqlite3.(haniokasai)
	16.04.28 0.0.1n
		-for nukkit.
 -thanks hmy2001! http://hmy2001.dip.jp/Blog/?m=201510
*/


public class Main extends PluginBase implements Listener{


	public void onEnable() {

		 this.getServer().getPluginManager().registerEvents(this, this);
		getDataFolder().mkdir();
		 // load the sqlite-JDBC driver using the current class loader

		try{
			Class.forName("org.sqlite.JDBC");
			}catch(Exception e){
				 System.err.println(e.getMessage());
			}
		   Connection connection = null;
		   try
		   {
		     connection = DriverManager.getConnection("jdbc:sqlite:"+getDataFolder()+"/killb.db");
		     Statement statement = connection.createStatement();
		     statement.executeUpdate("CREATE TABLE IF NOT EXISTS  block (xyz TEXT PRIMARY KEY, level TEXT , who TEXT ,ip TEXT, cid TEXT, action TEXT, time INT, blockname TEXT, blockid INT, meta INT ,skinid TEXT)");

		   }
		   catch(SQLException e)
		   {

		     System.err.println(e.getMessage());
		   }
		   finally
		   {
			   try
			      {
			        if(connection != null)
			          connection.close();
			      }
			      catch(SQLException e)
			      {
			        // connection close failed.
			        System.err.println(e);
			      }
		   }

        this.getServer().getLogger().info("[AdminNotice] Loaded");

}

	public void onDisable() {

	}

/*
@EventHandler
	public void t(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if(player.hasPermission("KillBearBoys.touch")){
			double x = event.getBlock().x;
			double y = event.getBlock().y;
			double z = event.getBlock().z;
			String xyz ="x"+x+"y"+y+"z"+z;
			try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT xyz, who ,level, ip, cid, action, blockname, blockid, meta, time ,skinid FROM block WHERE xyz = "+xyz);
			if(rs.getString("xyz") != null){

				player.sendMessage("[KillBearBoys]////////////////////");
				player.sendMessage("[XYZ] "+xyz);
				player.sendMessage("[Name] "+rs.getString("who"));
				player.sendMessage("[IP] "+rs.getString("ip")+" [CID] "+rs.getString("cid"));
				player.sendMessage("[Action]"+rs.getString("action"));
				player.sendMessage("[Block] "+rs.getString("blockname")+" [ID] "+rs.getInt("blockid")+ ":"+rs.getInt("meta"));
				player.sendMessage("[Date] "+rs.getInt("time")+"[SkinID]"+rs.getString("skinid"));
				player.sendMessage("////////////////////////////////");
			}}
		     catch(SQLException e)
		     {
		       // connection close failed.
		       System.err.println(e);
		     }
		     catch(Exception e)
		     {
		       // connection close failed.
		    	 player.sendMessage("[KillBearBoys]There are no log.");
		     }
			}else{
				player.sendMessage("[KillBearBoys]There are no log.");
			}

	}*/
	@EventHandler
	public void b(BlockBreakEvent event){
		String level = event.getPlayer().getLevel().getName();
		double x = event.getBlock().x;
		double y = event.getBlock().y;
		double z = event.getBlock().z;
		String xyz ="x"+x+"y"+y+"z"+z;
		Player player = event.getPlayer();
		String who = player.getName();
		String ip =  player.getAddress();
		String cid = player.getClientSecret();
		String action = "Place";

		int time =  (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
		String blockname = event.getBlock().getName();
		int blockid = event.getBlock().getId();
		int meta = event.getBlock().getDamage();
		String skinid=Base64.getEncoder()
        .encodeToString(player.getSkin().toString().getBytes()).substring(0,15);


		Connection connection = null;
	    try{
	    connection = DriverManager.getConnection("jdbc:sqlite:"+getDataFolder()+"/killb.db");
	    Statement statement = connection.createStatement();
	    statement.setQueryTimeout(30);  // set timeout to 30 sec.
	    statement.executeUpdate("INSERT INTO honey (xyz,level, who , ip, cid, action, time, blockname, blockid, meta ,skinid) VALUES ("+xyz+","+level+","+who+","+ip+","+cid+","+action+","+time+","+blockname+","+blockid+","+meta+","+skinid+")");
	    }catch(SQLException e){
	      System.err.println(e.getMessage());
	    }finally{try{
	        if(connection != null)
	          connection.close();}
	      catch(SQLException e){
	        System.err.println(e);}
	    }
	}

	@EventHandler
	public void p(BlockPlaceEvent event){
		String level = event.getPlayer().getLevel().getName();
		double x = event.getBlock().x;
		double y = event.getBlock().y;
		double z = event.getBlock().z;
		String xyz ="x"+x+"y"+y+"z"+z;
		Player player = event.getPlayer();
		String who = player.getName();
		String ip =  player.getAddress();
		String cid = player.getClientSecret();
		String action = "Place";
		int time =  (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
		String blockname = event.getBlock().getName();
		int blockid = event.getBlock().getId();
		int meta = event.getBlock().getDamage();
		String skinid=Base64.getEncoder()
        .encodeToString(player.getSkin().toString().getBytes()).substring(0,15);

		Connection connection = null;
	    try{
	    connection = DriverManager.getConnection("jdbc:sqlite:"+getDataFolder()+"/killb.db");
	    Statement statement = connection.createStatement();
	    statement.setQueryTimeout(30);  // set timeout to 30 sec.
	    statement.executeUpdate("INSERT INTO honey (xyz,level, who , ip, cid, action, time, blockname, blockid, meta ,skinid) VALUES ("+xyz+","+level+","+who+","+ip+","+cid+","+action+","+time+","+blockname+","+blockid+","+meta+","+skinid+")");
	    }catch(SQLException e){
	      System.err.println(e.getMessage());
	    }finally{try{
	        if(connection != null)
	          connection.close();}
	      catch(SQLException e){
	        System.err.println(e);}
	    }

	}



}