package com.whitemagic2014.command.impl.friend;

import com.whitemagic2014.command.FriendCommand;
import com.whitemagic2014.dao.BotDBDao;
import com.whitemagic2014.pojo.CommandProperties;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @Description: 查询数据库表信息
 * @author: magic chen
 * @date: 2020/8/21 17:53
 **/
@Component
public class CheckDBCommand implements FriendCommand {

    @Autowired
    BotDBDao dbDao;

    @Override
    public CommandProperties properties() {
        return new CommandProperties("checkDB");
    }

    @Value("${bot.admin}")
    Long admin;

    @Override
    public Message execute(Friend sender, ArrayList<String> args, MessageChain messageChain, Friend subject) {
        if (sender.getId() != admin) {
            return new PlainText("抱歉,你没有权限执行此操作");
        }
        if (args.size() >= 1) {
            String tableName = args.get(0);
            if (dbDao.checkTableExist(tableName)) {
                return new PlainText("表 [" + tableName + "] 存在");
            } else {
                return new PlainText("表 [" + tableName + "] 不存在");
            }
        } else {
            return new PlainText("数据库信息: " + dbDao.DBVersion().toString() + "\n" + "已有表: "
                    + dbDao.tables().stream().map(s -> "[" + s + "] ").reduce("", String::concat));
        }
    }
}