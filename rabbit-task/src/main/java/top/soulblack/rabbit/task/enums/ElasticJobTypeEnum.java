package top.soulblack.rabbit.task.enums;

/**
 * Created by lxf on 2020/10/13
 */
public enum ElasticJobTypeEnum {
    SIMPLE("SimpleJob", "简单类型job"),
    DATA_FLOW("DataflowJob", "流式类型job"),
    SCRIPT("ScriptJob", "脚本类型job");

    private final String type;

    private final String desc;

    ElasticJobTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static boolean isElasticJob(String simpleName) {
        for (ElasticJobTypeEnum simpleEnum : ElasticJobTypeEnum.values()) {
            if (simpleName.equals(simpleEnum.getType())) {
                return true;
            }
        }
        return false;
    }

    public static ElasticJobTypeEnum getType(String simpleName) {
        for (ElasticJobTypeEnum simpleEnum : ElasticJobTypeEnum.values()) {
            if (simpleEnum.getType().equals(simpleName)) {
                return simpleEnum;
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
