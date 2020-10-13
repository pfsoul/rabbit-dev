package top.soulblack.rabblit.task.enums;

/**
 * Created by lxf on 2020/10/13
 */
public enum ElasticJobTypeEnum {
    SIMPLE("SimpleJob", "简单类型job"),
    DATA_FLOW("DataFlowJob", "流式类型job"),
    SCRIPT("ScriptJob", "脚本类型job");

    private final String type;

    private final String desc;

    ElasticJobTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
