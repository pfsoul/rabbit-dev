package top.soulblack.rabbit.producer.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import top.soulblack.rabbit.api.model.Message;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by lxf on 2020/10/19
 */
public class MessageTypeHandler implements TypeHandler<Message> {


    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, Message message, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, JSON.toJSONString(message));
    }

    @Override
    public Message getResult(ResultSet resultSet, String s) throws SQLException {
        String value = resultSet.getString(s);
        if (StringUtils.isNotBlank(value)) {
            return JSON.parseObject(value, Message.class);
        }
        return null;
    }

    @Override
    public Message getResult(ResultSet resultSet, int i) throws SQLException {
        String value = resultSet.getString(i);
        if (StringUtils.isNotBlank(value)) {
            return JSON.parseObject(value, Message.class);
        }
        return null;
    }

    @Override
    public Message getResult(CallableStatement callableStatement, int i) throws SQLException {
        String value = callableStatement.getString(i);
        if (StringUtils.isNotBlank(value)) {
            return JSON.parseObject(value, Message.class);
        }
        return null;
    }
}
