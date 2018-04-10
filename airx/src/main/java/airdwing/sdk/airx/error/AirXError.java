package airdwing.sdk.airx.error;

import android.util.SparseArray;

import java.io.IOException;

import retrofit2.Response;

/**
 * Created by caojin on 2017/5/26.
 *
 * 用户看不懂的提示直接显示错误码
 */

public class AirXError {
    public static final String WRONG_SIGNATURE_METHOD = "wrong signature method(should either be HmacSHA1 or HmacSHA256)";
    public static final String SAVE_FILE_FAILED = "save file filed";
    public static final String UPLOAD_FILE_NOT_EXIST = "file not exist or is directory";
    private static SparseArray<String> errorMap = new SparseArray<>();

    public static String buildMsg(Response response) {
        String errorBody = "no detail";
        try {
            errorBody = response.errorBody().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return String.format("code: %d, msg: %s, detail: %s", response.code(), response.message(), errorBody);
    }

    public static String getDetail(int code) {
        String detail = errorMap.get(code);
        if (detail == null) {
            detail = "错误码：" + code;
        }
        return detail;
    }

    static {
        errorMap.put(1000, "未知错误");
        errorMap.put(1001, "参数不合法");
        errorMap.put(1100, "数据库错误");
        errorMap.put(1101, "微软基础服务错误");

        errorMap.put(2000, "短信验证码错误");
        errorMap.put(2001, "短信验证码发送限制");
        errorMap.put(2002, "用户名已经被注册");
        errorMap.put(2003, "已绑定用户名");
        errorMap.put(2004, "邮箱已被注册");
        errorMap.put(2005, "手机号已被注册");
        errorMap.put(2006, "组织名称已存在");
        errorMap.put(2007, "邮箱验证码错误");
        errorMap.put(2008, "邮箱验证码发送限制");
        errorMap.put(2009, "用户已经有了绑定邮箱");
        errorMap.put(2010, "用户名格式不合法");
        errorMap.put(2011, "错误验证码超过3次，需要重新发送");

        errorMap.put(2100, "密码强度不符合要求");
        errorMap.put(2101, "加密错误");
        errorMap.put(2102, "旧密码错误");

        errorMap.put(2200, "用户不存在或密码错误");
        errorMap.put(2201, "未授权的登录设备");
        errorMap.put(2202, "用户未拥有此设备");
        errorMap.put(2203, "登录超时或非法登录Auth Token");

        errorMap.put(2300, "用户已经加入/创建组织");
        errorMap.put(2301, "组织管理员不存在");
        errorMap.put(2302, "上级组织不存在");
        errorMap.put(2303, "组织不存在");
        errorMap.put(2304, "用户和组织不匹配");
        errorMap.put(2305, "用户手机号尚未注册");
        errorMap.put(2306, "待添加到用户不存在");
        errorMap.put(2307, "待删除的用户不存在");
        errorMap.put(2308, "用户未加入组织");
        errorMap.put(2309, "用户权限不足");
        errorMap.put(2310, "无权操作该组织");
        errorMap.put(2311, "无权操作上级组织");
        errorMap.put(2312, "用户邮箱尚未注册");
        errorMap.put(2313, "身份证号码不合法");
        errorMap.put(2314, "真实姓名不合法");
        errorMap.put(2315, "用户的认证已经通过或者正在等待验证");
        errorMap.put(2316, "过期时间不合法");
        errorMap.put(2317, "管理员将自己再次设为了本组织管理员");
        errorMap.put(2318, "更换管理员的对象不得已经为管理员");
        errorMap.put(2319, "组织和子组织内除了被移除的管理员自己，还有别的成员");
        errorMap.put(2320, "组织和子组织内还有设备未删除");
        errorMap.put(2321, "被添加为管理员的用户必须为组织内部成员");
        errorMap.put(2322, "用户的认证已经通过或者正在等待验证");

        errorMap.put(2400, "文件格式不合法");
        errorMap.put(2401, "文件类型不合法");
        errorMap.put(2402, "日志信息参数缺失");

        errorMap.put(2500, "该设备已经注册");
        errorMap.put(2501, "设备未注册");
        errorMap.put(2502, "设备iothub启用失败");
        errorMap.put(2503, "设备iothub禁用失败");
        errorMap.put(2504, "修改设备信息输入参数不可用");
        errorMap.put(2505, "设备或者北斗card已经被绑定");
        //errorMap.put(2506, "最后插入数据库的时候，并没有产生新的行，有可能是多个用户同时操作所致");
        errorMap.put(2507, "设备没有绑定到北斗card");
        //errorMap.put(2508, "删除此数据的时候，数据库没有行数变化，有可能是多个用户同时操作所致");
        errorMap.put(2509, "设备分配cpn错误，请重试");
        errorMap.put(2510, "此飞控编号已经注册");
        errorMap.put(2511, "此机身编号已经注册");
        errorMap.put(2512, "大疆设备必须输入fcsn");
        errorMap.put(2513, "组织内未找到对应设备");

        errorMap.put(2601, "创建录制任务失败");
        errorMap.put(2602, "已经申请的任务没有终止");
        errorMap.put(2603, "已经申请的任务已经终止");
        //errorMap.put(2604, "视频直播索引码无效");
        //errorMap.put(2605, "数据库中用户id和起飞时间已占用");
        //errorMap.put(2606, "数据库中oid上级递进查询出现问题，例子：出现子组织a的父组织b的父组织，又是a");
        errorMap.put(2607, "传递参数中，结束时间比开始时间要早，错误");
        errorMap.put(2608, "只能查一个小时之内的直播数据");
        //errorMap.put(2609, "新的history查询必须输入oid(原来的只需要did)");
        errorMap.put(2610, "任务未被查询到");

        errorMap.put(2700, "用户无权处理此app版本");
        errorMap.put(2701, "用户输入参数有误");
        errorMap.put(2702, "已经验证完毕，不能再次编辑");
        errorMap.put(2703, "目标app记录不存在");
        errorMap.put(2704, "APP版本已被删除");
        errorMap.put(2705, "appkey对应的app找不到可用版本");
        errorMap.put(2706, "组织用户数据错误");

        errorMap.put(2800, "找不到目标绑定信息");
        errorMap.put(2801, "绑定信息对象解析错误");

        errorMap.put(2900, "经纬度错误错误");
        errorMap.put(2901, "生成禁飞区或者飞行区域id错误");
        errorMap.put(2902, "禁飞区id查询失败");
        errorMap.put(2903, "内部禁飞区，不需要国家审批，国家也不能删除");
        //errorMap.put(2904, "审批或者删除没有改变到数据库数据，有可能是操作已经过时了");
        //errorMap.put(2905, "pass字段传值有误");

        //errorMap.put(3000, "插入飞行区域申请数据库失败");
        //errorMap.put(3001, "审批没有改变到数据库数据，有可能是操作以及过时了");
        errorMap.put(3002, "输入的设备有不在组织设备列表中的");

        //errorMap.put(3101, "用户auth的组织号和实际组织号不匹配");
        errorMap.put(3102, "logo等信息已经存在，不可重复添加");
        errorMap.put(3103, "logo等信息已经不存在，不可重复删除");
        errorMap.put(3104, "税号必须为15，18或20位");
        errorMap.put(3105, "组织已经提交过申请，等待批复或者申请已经被批准");
        errorMap.put(3106, "找不到要查找的申请记录");

        //errorMap.put(4100, "鉴权失败");
        //errorMap.put(4104, "不存在的SecretId");

        //errorMap.put(4500, "重放攻击");

        errorMap.put(4601, "不能操作其他组织");
        errorMap.put(4602, "不能操作其他人的事务");
    }
}
