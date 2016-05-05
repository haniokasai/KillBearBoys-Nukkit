package com.haniokasai.nukkit.KillBearBoys;

import java.io.File;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;


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

	private static HashMap<String,String> ppp = new HashMap<String,String>();

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
		     statement.executeUpdate("CREATE TABLE IF NOT EXISTS  block (xyz TEXT PRIMARY KEY, level TEXT , who TEXT ,ip TEXT, cid TEXT, action TEXT, time TEXT, blockname TEXT, blockid INT, meta INT ,skinid TEXT)");

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


        this.getServer().getLogger().info("[KillBearBoys] Loaded");

}

	public void onDisable() {

	}


@EventHandler
	public void t(BlockPlaceEvent event){
		Player player = event.getPlayer();
		if(ppp.containsKey(player.getName())){
			event.setCancelled();
		String x = String.valueOf(Math.round(event.getBlock().x));
		String y = String.valueOf(Math.round(event.getBlock().y));
		String z = String.valueOf(Math.round(event.getBlock().z));
		String xyz =("x"+x+"y"+y+"z"+z);
		Connection connection = null;
			try {
			    connection = DriverManager.getConnection("jdbc:sqlite:"+getDataFolder()+"/killb.db");
			    Statement statement = connection.createStatement();
			    statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT xyz, who ,level, ip, cid, action, blockname, blockid, meta, time ,skinid FROM block WHERE xyz = '"+xyz+"'");
			if(rs.getString("xyz") != null){

				player.sendMessage("[KillBearBoys]////////////////////");
				player.sendMessage("[XYZ] "+xyz);
				player.sendMessage("[Name] "+rs.getString("who"));
				player.sendMessage("[IP] "+rs.getString("ip")+" [CID] "+rs.getString("cid"));
				player.sendMessage("[Action]"+rs.getString("action"));
				player.sendMessage("[Block] "+rs.getString("blockname")+" [ID] "+rs.getInt("blockid")+ ":"+rs.getInt("meta"));
				player.sendMessage("[Date] "+rs.getString("time")+"[SkinID]"+rs.getString("skinid"));
				player.sendMessage("////////////////////////////////");
			}}
		     catch(SQLException e)
		     {

		      // System.err.println(e);
		     }
		     catch(Exception e)
		     {
		       // connection close failed.
		    	 player.sendMessage("[KillBearBoys]There are no log.");
		     }
		finally{try{
	        if(connection != null)
	          connection.close();}
	      catch(SQLException e){
	        System.err.println(e);}
	    }
			}else{
				String level = event.getPlayer().getLevel().getName();
				String x = String.valueOf(Math.round(event.getBlock().x));
				String y = String.valueOf(Math.round(event.getBlock().y));
				String z = String.valueOf(Math.round(event.getBlock().z));
				String xyz =("x"+x+"y"+y+"z"+z);
				///////doubleじゃらめー－－－－－
				String who = player.getName();
				String ip =  player.getAddress();
				String cid = player.getClientSecret();
				String action = "Place";
				String blockname = event.getBlock().getName();
				int blockid = event.getBlock().getId();
				int meta = event.getBlock().getDamage();


				//skin
				String skinid = null;
				try {
					MessageDigest md5 = MessageDigest.getInstance("MD5");
					byte[] result = md5.digest(player.getSkin().toString().getBytes().toString().getBytes());
					skinid = new String(result, "UTF-8");
				} catch (Exception e1) {
				}
				///

				Connection connection = null;
			    try{
			    connection = DriverManager.getConnection("jdbc:sqlite:"+getDataFolder()+"/killb.db");
			    Statement statement = connection.createStatement();
			    statement.setQueryTimeout(30);  // set timeout to 30 sec.
			    statement.executeUpdate("INSERT OR REPLACE INTO block (xyz,level, who , ip, cid, action, time, blockname, blockid, meta ,skinid) VALUES ('"+xyz+"','"+level+"','"+who+"','"+ip+"','"+cid+"','"+action+"',datetime('now', 'localtime'),'"+blockname+"',"+blockid+","+meta+",'"+skinid+"')");
			    }catch(SQLException e){
			      System.err.println(e.getMessage());
			    }finally{try{
			        if(connection != null)
			          connection.close();}
			      catch(SQLException e){
			        System.err.println(e);event.setCancelled();}
			    }
			}

	}
	@EventHandler
	public void b(BlockBreakEvent event){
		Player player = event.getPlayer();
		if(ppp.containsKey(player.getName())){
			event.setCancelled();
		String x = String.valueOf(Math.round(event.getBlock().x));
		String y = String.valueOf(Math.round(event.getBlock().y));
		String z = String.valueOf(Math.round(event.getBlock().z));
		String xyz =("x"+x+"y"+y+"z"+z);
		Connection connection = null;
			try {
			    connection = DriverManager.getConnection("jdbc:sqlite:"+getDataFolder()+"/killb.db");
			    Statement statement = connection.createStatement();
			    statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT xyz, who ,level, ip, cid, action, blockname, blockid, meta, time ,skinid FROM block WHERE xyz = '"+xyz+"'");
			if(rs.getString("xyz") != null){

				player.sendMessage("[KillBearBoys]////////////////////");
				player.sendMessage("[XYZ] "+xyz);
				player.sendMessage("[Name] "+rs.getString("who"));
				player.sendMessage("[IP] "+rs.getString("ip")+" [CID] "+rs.getString("cid"));
				player.sendMessage("[Action]"+rs.getString("action"));
				player.sendMessage("[Block] "+rs.getString("blockname")+" [ID] "+rs.getInt("blockid")+ ":"+rs.getInt("meta"));
				player.sendMessage("[Date] "+rs.getString("time")+"[SkinID]"+rs.getString("skinid"));
				player.sendMessage("////////////////////////////////");
			}}
		     catch(SQLException e)
		     {

		      // System.err.println(e);
		     }
		     catch(Exception e)
		     {
		       // connection close failed.
		    	 player.sendMessage("[KillBearBoys]There are no log.");
		     }
		finally{try{
	        if(connection != null)
	          connection.close();}
	      catch(SQLException e){
	        System.err.println(e);}
	    }
			}else{

	    String level = event.getPlayer().getLevel().getName();
		String x = String.valueOf(Math.round(event.getBlock().x));
		String y = String.valueOf(Math.round(event.getBlock().y));
		String z = String.valueOf(Math.round(event.getBlock().z));
		String xyz =("x"+x+"y"+y+"z"+z);

		String who = player.getName();
		String ip =  player.getAddress();
		String cid = player.getClientSecret();
		String action = "Place";
		String blockname = event.getBlock().getName();
		int blockid = event.getBlock().getId();
		int meta = event.getBlock().getDamage();

		//skin
		String skinid = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] result = md5.digest(player.getSkin().toString().getBytes().toString().getBytes());
			skinid = new String(result, "UTF-8");
		} catch (Exception e1) {
		}
		///


		Connection connection = null;
	    try{
	    connection = DriverManager.getConnection("jdbc:sqlite:"+getDataFolder()+"/killb.db");
	    Statement statement = connection.createStatement();
	    statement.setQueryTimeout(30);  // set timeout to 30 sec.
	    statement.executeUpdate("INSERT OR REPLACE INTO block (xyz,level, who , ip, cid, action, time, blockname, blockid, meta ,skinid) VALUES ('"+xyz+"','"+level+"','"+who+"','"+ip+"','"+cid+"','"+action+"',datetime('now', 'localtime'),'"+blockname+"',"+blockid+","+meta+",'"+skinid+"')");
	    }catch(SQLException e){
	      System.err.println(e.getMessage());
	    }finally{try{
	        if(connection != null)
	          connection.close();}
	      catch(SQLException e){
	        System.err.println(e);}
	    }}

	}

	public void onmotion(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if(ppp.containsKey(player.getName())){
			event.setCancelled();
		String x = String.valueOf(Math.round(event.getBlock().x));
		String y = String.valueOf(Math.round(event.getBlock().y));
		String z = String.valueOf(Math.round(event.getBlock().z));
		String xyz =("x"+x+"y"+y+"z"+z);
		Connection connection = null;
			try {
			    connection = DriverManager.getConnection("jdbc:sqlite:"+getDataFolder()+"/killb.db");
			    Statement statement = connection.createStatement();
			    statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT xyz, who ,level, ip, cid, action, blockname, blockid, meta, time ,skinid FROM block WHERE xyz = '"+xyz+"'");
			if(rs.getString("xyz") != null){

				player.sendMessage("[KillBearBoys]////////////////////");
				player.sendMessage("[XYZ] "+xyz);
				player.sendMessage("[Name] "+rs.getString("who"));
				player.sendMessage("[IP] "+rs.getString("ip")+" [CID] "+rs.getString("cid"));
				player.sendMessage("[Action]"+rs.getString("action"));
				player.sendMessage("[Block] "+rs.getString("blockname")+" [ID] "+rs.getInt("blockid")+ ":"+rs.getInt("meta"));
				player.sendMessage("[Date] "+rs.getString("time")+"[SkinID]"+rs.getString("skinid"));
				player.sendMessage("////////////////////////////////");
			}}
		     catch(SQLException e)
		     {

		      // System.err.println(e);
		     }
		     catch(Exception e)
		     {
		       // connection close failed.
		    	 player.sendMessage("[KillBearBoys]There are no log.");
		     }
		finally{try{
	        if(connection != null)
	          connection.close();}
	      catch(SQLException e){
	        System.err.println(e);}
	    }
			}
	}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	Config config = new Config(new File(this.getDataFolder(), "config.yml"));
        switch (command.getName()) {
            case "co":
        		if(!sender.hasPermission("KillBearBoys.touch")){
        			sender.sendMessage(TextFormat.RED + "You don't have permission to use this command.");
        			return false;
        		}else{
        			 //System.out.println(config.get("enable-core"));
        			if(!ppp.containsKey(sender.getName())){
        				ppp.put(sender.getName(),"true");
        				sender.sendMessage(TextFormat.GREEN +"[KillBearBoys] Enable.");
        			}else{
        				sender.sendMessage(TextFormat.GREEN +"[KillBearBoys] exit.");
        				ppp.remove(sender.getName());
        			}
        			config.save();
        		}
            break;
        }
		return true;}
}