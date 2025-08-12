package com.example.morpheus.plugin;

import com.example.morpheus.provider.SecuritySustainabilityInstanceTabProvider;
import com.morpheusdata.core.Plugin;
import com.morpheusdata.model.OptionType;
import com.morpheusdata.model.OptionType.InputType;
import com.morpheusdata.views.HandlebarsRenderer;

import java.util.ArrayList;
import java.util.List;

public class SecuritySustainabilityTabPlugin extends Plugin {

    @Override
    public String getCode() {
        return "security-sustainability-tab";
    }

    @Override
    public void initialize() {
        this.setName("Security & Sustainability Tab Plugin");
        this.setDescription("Adds an Instance tab with cloud-specific security and sustainability links.");
        this.setRenderer(new HandlebarsRenderer(this.getClassLoader()));
        this.registerProvider(new SecuritySustainabilityInstanceTabProvider(this, morpheus));
        this.setSettings(buildSettings());
    }

    private List<OptionType> buildSettings() {
        List<OptionType> options = new ArrayList<>();

        OptionType awsSecurityRegion = new OptionType();
        awsSecurityRegion.setName("AWS Security Region");
        awsSecurityRegion.setCode("AWS_SECURITY_REGION");
        awsSecurityRegion.setFieldName("AWS_SECURITY_REGION");
        awsSecurityRegion.setInputType(InputType.TEXT);
        awsSecurityRegion.setDefaultValue("ap-southeast-1");
        awsSecurityRegion.setDisplayOrder(0);
        awsSecurityRegion.setRequired(false);
        options.add(awsSecurityRegion);

        OptionType awsSustainabilityRegion = new OptionType();
        awsSustainabilityRegion.setName("AWS Sustainability Region");
        awsSustainabilityRegion.setCode("AWS_SUSTAINABILITY_REGION");
        awsSustainabilityRegion.setFieldName("AWS_SUSTAINABILITY_REGION");
        awsSustainabilityRegion.setInputType(InputType.TEXT);
        awsSustainabilityRegion.setDefaultValue("us-east-1");
        awsSustainabilityRegion.setDisplayOrder(1);
        awsSustainabilityRegion.setRequired(false);
        options.add(awsSustainabilityRegion);

        OptionType hideWhenNotApplicable = new OptionType();
        hideWhenNotApplicable.setName("Hide When Not Applicable");
        hideWhenNotApplicable.setCode("HIDE_WHEN_NOT_APPLICABLE");
        hideWhenNotApplicable.setFieldName("HIDE_WHEN_NOT_APPLICABLE");
        hideWhenNotApplicable.setInputType(InputType.CHECKBOX);
        hideWhenNotApplicable.setDefaultValue("true");
        hideWhenNotApplicable.setDisplayOrder(2);
        hideWhenNotApplicable.setRequired(false);
        options.add(hideWhenNotApplicable);

        return options;
    }

    @Override
    public void onDestroy() {
        // no-op
    }
}