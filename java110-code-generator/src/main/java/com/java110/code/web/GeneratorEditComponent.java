package com.java110.code.web;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.code.back.BaseGenerator;
import org.springframework.util.StringUtils;

public class GeneratorEditComponent extends BaseGenerator {

    public void generator(JSONObject data) {

        //处理组件
        generatorComponentHtml(data);
        generatorComponentJs(data);
//        generatorComponentJava(data);
//        genneratorIListSmo(data);
//        genneratorListSmoImpl(data);
//        genneratorListListener(data);



    }

    /**
     * 生成 html js java 类
     *
     * @param data
     */
    private void generatorComponentHtml(JSONObject data) {

        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/edit/edit.html").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        // 处理 th 信息

        StringBuffer thSb = new StringBuffer();

        JSONArray columns = data.getJSONArray("columns");
        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
            JSONObject column = columns.getJSONObject(columnIndex);
            if ("none".equals(column.getString("inputType"))) {
                continue;
            }
            String required = column.getBoolean("required") ? "必填" : "选填";
            String inputStr = "";
            if ("select".equals(column.getString("inputType"))) {

                String[] selectValues = column.getString("selectValue").split(",");
                String[] selectValueNames = column.getString("selectValueName").split(",");


                String option = "";
                for (int valueIndex = 0; valueIndex < selectValues.length; valueIndex++) {

                    String value = selectValues[valueIndex];

                    option += "<option  value=\"" + value + "\">" + selectValueNames[valueIndex] + "</option>\n";

                }

                inputStr = "<select class=\"custom-select\" v-model=\"edit" + toUpperCaseFirstOne(data.getString("templateCode")) + "Info."+column.getString("code")+"\">\n" +
                        "         <option selected  disabled value=\"\">"+ required + "，请选择" + column.getString("cnCode") + "</option>\n" +
                        "         " +option+
                        "  </select>";
            } else if("textarea".equals(column.getString("inputType"))){
                inputStr = "<textarea  placeholder=\"" + required + "，请填写" + column.getString("cnCode") + "\" class=\"form-control\""+
                        " v-model=\"edit" + toUpperCaseFirstOne(data.getString("templateCode")) + "Info."+column.getString("code")+"\">"+
                        "</textarea>";
            }else {
                inputStr = "           <input v-model=\"edit" + toUpperCaseFirstOne(data.getString("templateCode")) + "Info."+column.getString("code")+"\" " +
                        "                  type=\"text\" placeholder=\"" + required + "，请填写" + column.getString("cnCode") + "\" class=\"form-control\">\n";
            }
            thSb.append("<div class=\"form-group row\">\n" +
                    "         <label class=\"col-sm-2 col-form-label\">" + column.getString("cnCode") + "</label>\n" +
                    "         <div class=\"col-sm-10\">\n" +
                    inputStr+
                    "         </div>\n" +
                    "</div>\n");

        }

        fileContext = fileContext.replace("@@editTemplateColumns@@", thSb.toString());


        String writePath = this.getClass().getResource("/").getPath()
                + "out/web/component/"+data.getString("templateCode")+"Package/edit" + toUpperCaseFirstOne(data.getString("templateCode")) + "/edit" + toUpperCaseFirstOne(data.getString("templateCode")) + ".html";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);


    }

    /**
     * 生成 html js java 类
     *
     * @param data
     */
    private void generatorComponentJs(JSONObject data) {

        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/edit/edit.js").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        //替换 变量@@templateCodeColumns@@
        JSONArray columns = data.getJSONArray("columns");

        StringBuffer variable = new StringBuffer();
        variable.append(data.getString("templateKey")+":'',\n");
        String defaultValue = "";


        String validateInfo = "";
        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
            JSONObject column = columns.getJSONObject(columnIndex);
            defaultValue = column.getBoolean("hasDefaultValue") ? column.getString("defaultValue") : "";
            defaultValue = "'" + defaultValue + "'";
            variable.append(column.getString("code") + ":" + defaultValue + ",\n");

            validateInfo += "'edit"+toUpperCaseFirstOne(data.getString("templateCode"))+"Info."+column.getString("code")+"':[\n" ;
            if(column.getBoolean("required")) {
                validateInfo +="{\n" +
                        "                            limit:\"required\",\n" +
                        "                            param:\"\",\n" +
                        "                            errInfo:\""+column.getString("cnCode")+"不能为空\"\n" +
                        "                        },\n";
            }

            if(column.containsKey("limit") && !StringUtils.isEmpty(column.getString("limit"))) {
                validateInfo +=" {\n" +
                        "                            limit:\""+column.getString("limit")+"\",\n" +
                        "                            param:\""+column.getString("limitParam")+"\",\n" +
                        "                            errInfo:\""+column.getString("limitErrInfo")+"\"\n" +
                        "                        },\n" +
                        "                    ],\n";
            }

        }
        validateInfo += "'edit"+toUpperCaseFirstOne(data.getString("templateCode"))+"Info."+data.getString("templateKey")+"':[\n" ;
        validateInfo +="{\n" +
                "                            limit:\"required\",\n" +
                "                            param:\"\",\n" +
                "                            errInfo:\""+data.getString("templateKeyName")+"不能为空\"\n" +
                "                        }]\n";
        fileContext =  fileContext.replace("@@templateCodeColumns@@", variable.toString());
        fileContext = fileContext.replace("@@editTemplateCodeValidate@@", validateInfo);

        // 替换 数据校验部分代码


        String writePath = this.getClass().getResource("/").getPath()
                + "out/web/component/"+data.getString("templateCode")+"Package/edit" + toUpperCaseFirstOne(data.getString("templateCode")) + "/edit" + toUpperCaseFirstOne(data.getString("templateCode")) + ".js";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);


    }

    /**
     * 生成 html js java 类
     *
     * @param data
     */
    private void generatorComponentJava(JSONObject data) {

        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/edit/EditComponent.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/web/component/java/" + data.getString("templateCode") + "/Edit" + toUpperCaseFirstOne(data.getString("templateCode")) + "Component.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);


    }

    /**
     * 生成接口类
     *
     * @param data
     */
    private void genneratorIListSmo(JSONObject data) {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/edit/IEditSMO.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/web/smo/" + data.getString("templateCode") + "/IEdit" + toUpperCaseFirstOne(data.getString("templateCode")) + "SMO.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
    }

    /**
     * 生成接口类
     *
     * @param data
     */
    private void genneratorListSmoImpl(JSONObject data) {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/edit/EditSMOImpl.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        //替换校验部分代码 @@validateTemplateColumns@@
        JSONArray columns = data.getJSONArray("columns");
        StringBuffer validateStr = new StringBuffer();

        validateStr.append("Assert.hasKeyAndValue(paramIn, \""+data.getString("templateKey")+"\", \""+data.getString("templateKeyName")+"不能为空\");\n");
        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
            JSONObject column = columns.getJSONObject(columnIndex);
            if(column.getBoolean("required")) {
                validateStr.append("Assert.hasKeyAndValue(paramIn, \""+column.getString("code")+"\", \""+column.getString("desc")+"\");\n");
            }
        }

        fileContext = fileContext.replace("@@validateTemplateColumns@@", validateStr.toString());


        String writePath = this.getClass().getResource("/").getPath()
                + "out/web/smo/" + data.getString("templateCode") + "/impl/Edit" + toUpperCaseFirstOne(data.getString("templateCode")) + "SMOImpl.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
    }

    /**
     * 生成API 侦听处理类
     *
     * @param data
     */
    private void genneratorListListener(JSONObject data) {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/edit/UpdateListener.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        //替换校验部分代码 @@validateTemplateColumns@@
        JSONArray columns = data.getJSONArray("columns");
        StringBuffer validateStr = new StringBuffer();
        validateStr.append("Assert.hasKeyAndValue(reqJson, \""+data.getString("templateKey")+"\", \""+data.getString("templateKeyName")+"不能为空\");\n");
        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
            JSONObject column = columns.getJSONObject(columnIndex);
            if(column.getBoolean("required")) {
                validateStr.append("Assert.hasKeyAndValue(reqJson, \""+column.getString("code")+"\", \""+column.getString("desc")+"\");\n");
            }
        }

        fileContext = fileContext.replace("@@validateTemplateColumns@@", validateStr.toString());


        String writePath = this.getClass().getResource("/").getPath()
                + "out/api/listener/" + data.getString("templateCode") + "/Update" + toUpperCaseFirstOne(data.getString("templateCode")) + "Listener.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
    }



}
