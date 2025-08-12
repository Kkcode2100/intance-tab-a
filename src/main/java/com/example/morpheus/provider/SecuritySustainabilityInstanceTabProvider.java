package com.example.morpheus.provider;

import com.example.morpheus.service.CloudButtonMapper;
import com.example.morpheus.service.CloudButtonMapper.CloudLink;
import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.Plugin;
import com.morpheusdata.core.providers.InstanceTabProvider;
import com.morpheusdata.model.Account;
import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.Instance;
import com.morpheusdata.model.User;
import com.morpheusdata.views.HandlebarsRenderer;
import com.morpheusdata.views.HTMLResponse;
import com.morpheusdata.views.Renderer;
import com.morpheusdata.views.ViewModel;
import io.reactivex.rxjava3.core.Observable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SecuritySustainabilityInstanceTabProvider implements InstanceTabProvider {

    private final Plugin plugin;
    private final MorpheusContext morpheus;

    public SecuritySustainabilityInstanceTabProvider(Plugin plugin, MorpheusContext morpheus) {
        this.plugin = plugin;
        this.morpheus = morpheus;
    }

    @Override
    public String getCode() {
        return "securitySustainabilityTab";
    }

    @Override
    public String getName() {
        return "Security & Sustainability";
    }

    @Override
    public Boolean show(Instance instance, User user, Account account) {
        String cloudType = resolveCloudType(instance);
        boolean isApplicable = isSupportedCloud(cloudType);
        boolean hideWhenNotApplicable = parseBoolean(getPluginSetting("HIDE_WHEN_NOT_APPLICABLE"), true);
        return isApplicable || !hideWhenNotApplicable;
    }

    @Override
    public HTMLResponse renderTemplate(Instance instance) {
        String cloudType = resolveCloudType(instance);
        String awsSecurityRegion = getPluginSettingOrDefault("AWS_SECURITY_REGION", "ap-southeast-1");
        String awsSustainabilityRegion = getPluginSettingOrDefault("AWS_SUSTAINABILITY_REGION", "us-east-1");

        List<CloudLink> cloudLinks = CloudButtonMapper.mapLinks(cloudType, awsSecurityRegion, awsSustainabilityRegion);

        List<Map<String, String>> links = new ArrayList<>();
        for (CloudLink link : cloudLinks) {
            Map<String, String> m = new HashMap<>();
            m.put("label", link.label);
            m.put("href", link.href);
            links.add(m);
        }

        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("links", links);
        if (!isSupportedCloud(cloudType)) {
            modelMap.put("info", "Not applicable for this instance.");
        }

        ViewModel<Map<String, Object>> model = new ViewModel<>();
        model.opts = modelMap;
        return getRenderer().renderTemplate("renderer/hbs/instanceTab", model);
    }

    @Override
    public Renderer<?> getRenderer() {
        return new HandlebarsRenderer(this.plugin.getClassLoader());
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public MorpheusContext getMorpheus() {
        return this.morpheus;
    }

    private boolean isSupportedCloud(String cloudTypeRaw) {
        if (cloudTypeRaw == null) return false;
        String type = cloudTypeRaw.trim().toLowerCase(Locale.ROOT);
        return type.equals("amazon") || type.equals("azure") || type.equals("google");
    }

    private String resolveCloudType(Instance instance) {
        try {
            if (instance == null) return null;
            Observable<Cloud> obs = morpheus.getInstance().getInstanceClouds(instance);
            Cloud cloud = obs != null ? obs.blockingFirst(null) : null;
            if (cloud == null || cloud.getCloudType() == null) return null;
            String code = cloud.getCloudType().getCloud();
            return code != null ? code.trim().toLowerCase(Locale.ROOT) : null;
        } catch (Throwable t) {
            return null;
        }
    }

    private String getPluginSettingOrDefault(String key, String defaultValue) {
        String v = getPluginSetting(key);
        return (v == null || v.isBlank()) ? defaultValue : v;
    }

    private String getPluginSetting(String key) {
        try {
            String settingsJson = morpheus.getSettings(plugin).blockingGet();
            if (settingsJson == null) return null;
            return extractJsonStringValue(settingsJson, key);
        } catch (Throwable t) {
            return null;
        }
    }

    private boolean parseBoolean(String raw, boolean defaultValue) {
        if (raw == null || raw.isBlank()) return defaultValue;
        String v = raw.trim().toLowerCase(Locale.ROOT);
        return v.equals("true") || v.equals("on") || v.equals("1") || v.equals("yes");
    }

    // Minimal JSON extractor for top-level string fields: "KEY":"VALUE"
    private String extractJsonStringValue(String json, String key) {
        String needle = "\"" + key + "\"";
        int idx = json.indexOf(needle);
        if (idx < 0) return null;
        int colon = json.indexOf(':', idx + needle.length());
        if (colon < 0) return null;
        int firstQuote = json.indexOf('"', colon + 1);
        if (firstQuote < 0) return null;
        int secondQuote = json.indexOf('"', firstQuote + 1);
        if (secondQuote < 0) return null;
        return json.substring(firstQuote + 1, secondQuote);
    }
}