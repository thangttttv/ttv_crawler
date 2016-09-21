package com.az24.util.io;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SwapHubDAO {

	public int saveMessageQueue(ChatMessage chatMessage) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0SwapHubPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection
					.prepareStatement("INSERT INTO vtc_swaphub.ms_chat_message_queue "
							+ " (fuID,tuID,pID,p_swap_ids,content,price, "
							+ " quantity,ship,file_path,type_message,time_receive)"
							+ " VALUES(?,?,?,?,?,?,?,?,?,?,NOW())");

			ps.setInt(1, chatMessage.getFuID());
			ps.setInt(2, chatMessage.getTuID());
			ps.setInt(3, chatMessage.getpID());
			ps.setString(4, chatMessage.getP_swap_id());
			ps.setString(5, chatMessage.getContent());
			ps.setDouble(6, chatMessage.getPrice());
			ps.setInt(7, chatMessage.getQuatity());
			ps.setInt(8, chatMessage.getShip());
			ps.setString(9, chatMessage.getFile());
			ps.setInt(10, chatMessage.getType());
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);
			String sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);

			C3p0SwapHubPool.attemptClose(resultSet);
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	public int saveMessage(ChatMessage chatMessage) {
		PreparedStatement ps;
		int id = 0;
		try {
			Connection connection = C3p0SwapHubPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection
					.prepareStatement("INSERT INTO vtc_swaphub.ms_chat_message "
							+ " (fuID,tuID,pID,p_swap_ids,content,price, "
							+ " quantity,ship,file_path,type_message,time_receive)"
							+ " VALUES(?,?,?,?,?,?,?,?,?,?,NOW())");

			ps.setInt(1, chatMessage.getFuID());
			ps.setInt(2, chatMessage.getTuID());
			ps.setInt(3, chatMessage.getpID());
			ps.setString(4, chatMessage.getP_swap_id());
			ps.setString(5, chatMessage.getContent());
			ps.setDouble(6, chatMessage.getPrice());
			ps.setInt(7, chatMessage.getQuatity());
			ps.setInt(8, chatMessage.getShip());
			ps.setString(9, chatMessage.getFile());
			ps.setInt(10, chatMessage.getType());
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);
			String sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);

			C3p0SwapHubPool.attemptClose(resultSet);
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	public int deleteMessageQueue(int id) {
		PreparedStatement ps;
		int kq = 0;
		try {
			Connection connection = C3p0SwapHubPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection
					.prepareStatement("DELETE FROM vtc_swaphub.ms_chat_message_queue WHERE id =  "
							+ id);
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);
			kq = 1;
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(connection);
			Thread.sleep(1000);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return kq;
	}

	public ArrayList<ChatMessage> getListMessageQueue(int userID) {
		PreparedStatement ps;
		ChatMessage chatMessage = null;
		ArrayList<ChatMessage> listMatch = new ArrayList<ChatMessage>();
		try {
			Connection connection = C3p0SwapHubPool.getConnection();

			ps = connection.prepareStatement("SELECT id,fuID, tuID, pID, p_swap_ids, content, "
							+ "price, quantity,ship,file_path,type_message,time_receive FROM "
							+ "vtc_swaphub.ms_chat_message_queue Where tuID = "
							+ userID + " Order by id  LIMIT 0, 100 ");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				chatMessage = new ChatMessage();
				chatMessage.id = rs.getInt("id");
				chatMessage.fuID = rs.getInt("fuID");
				chatMessage.tuID = rs.getInt("tuID");
				chatMessage.pID = rs.getInt("pID");
				chatMessage.p_swap_id = rs.getString("p_swap_ids");
				chatMessage.content = rs.getString("content");
				chatMessage.price = rs.getDouble("price");
				chatMessage.quantity = rs.getInt("quantity");
				chatMessage.ship = rs.getInt("ship");
				chatMessage.file = rs.getString("file_path");
				chatMessage.type = rs.getInt("type_message");
				chatMessage.time_receive = rs.getString("time_receive");
				listMatch.add(chatMessage);

			}
			C3p0SwapHubPool.attemptClose(rs);
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listMatch;
	}
}
