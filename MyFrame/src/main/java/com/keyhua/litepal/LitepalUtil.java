package com.keyhua.litepal;

import com.keyhua.renameyourself.util.CommonUtility;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 曾金叶 on 2016/4/12.
 */
public class LitepalUtil {
    //查询所有队员
    public static List<SignUpUser> getAllUser() {
        return DataSupport.findAll(SignUpUser.class);
    }

    //根据id查询指定用户
    public static SignUpUser getAllUserByid(long id) {
        return DataSupport.find(SignUpUser.class, id);
    }   //根据id查询指定用户

    //根据tps_idid查询指定用户
    public static SignUpUser getAllUserByTpsid(String tps_id) {
        if (DataSupport.where("tps_id=?", tps_id).find(SignUpUser.class).size() > 0) {
            return DataSupport.where("tps_id=?", tps_id).find(SignUpUser.class).get(0);
        } else {
            return null;
        }

    }


    //查找领队
    public static SignUpUser getLeader() {
        return DataSupport.where("tps_type= ?", String.valueOf(CommonUtility.LINGDUI)).find(SignUpUser.class).get(0);
    }  //查找是否崔在领队

    public static List<SignUpUser> getHasLeader() {
        return DataSupport.where("tps_type= ?", String.valueOf(CommonUtility.LINGDUI)).find(SignUpUser.class);
    }

    //查找所有队员
    public static List<SignUpUser> getDuiyuan() {
        return DataSupport.where("tps_type=?", String.valueOf(CommonUtility.DUIYUAN)).find(SignUpUser.class);
    }

    //查找所有未离队并且有设备的的队员
    public static List<SignUpUser> getAllUserByActisleave() {
//        return DataSupport.where("act_isleave= ? and deviceReady= ?", String.valueOf(CommonUtility.GUIDUI), String.valueOf(1)).order("distance DESC").find(SignUpUser.class);
        return DataSupport.where("act_isleave= ? ", String.valueOf(CommonUtility.GUIDUI)).order("distance DESC").find(SignUpUser.class);
    }

    //查找所有队员以距离排序
    public static List<SignUpUser> getAllUserByActisleaveDrderByDistance() {
        //不加DESC就是降序
        return DataSupport.order("distance DESC").find(SignUpUser.class);
    }

    //清除指定用户
    public static int deleteUser(int tps_type) {
        DataSupport.deleteAll(GpsInfo.class);
        return DataSupport.deleteAll(SignUpUser.class, "tps_type = ?", String.valueOf(tps_type));
    }    //清除指定用户

    public static int deleteAll() {
        DataSupport.deleteAll(GpsInfo.class);
        DataSupport.deleteAll(PlanGpsInfo.class);
        return DataSupport.deleteAll(SignUpUser.class);
    }


    //收队时清除除了领队以外的所有用户
    public static int deleteAllBesideLeader() {
        DataSupport.deleteAll(GpsInfo.class);
        DataSupport.deleteAll(PlanGpsInfo.class);
        return DataSupport.deleteAll(SignUpUser.class, "tps_type = ?", String.valueOf(CommonUtility.DUIYUAN));
    }

    public static int deletePlanGps() {
        return DataSupport.deleteAll(PlanGpsInfo.class);
    }

    //查询当前的计划轨迹
    public static PlanGpsInfo getpg() {
        if (DataSupport.findAll(PlanGpsInfo.class) != null && DataSupport.findAll(PlanGpsInfo.class).size() != 0) {
            return DataSupport.findAll(PlanGpsInfo.class).get(0);
        } else {
            return null;
        }
    }

    //获取当前需要上传的轨迹
//    public static List<GpsInfo> getUpdataGpsInfo() {
//        return DataSupport.where("name!= ? and start_time!= ? and end_time!= ?", "null,null,null").find(GpsInfo.class);
//    }
}
