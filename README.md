# Morpheus Cloud Links Instance Tab

A Morpheus plugin that adds a custom Instance tab showing cloud-specific Security and Sustainability links for AWS, Azure, and GCP.

## Build & Install

Local build:

```bash
./gradlew clean shadowJar
```

Install in Morpheus UI: Administration → Integrations → Plugins → Upload the `morpheus-cloud-links-tab-<version>-all.jar`.

Release flow:

```perl
git tag v1.0.0
git push origin v1.0.0
```

Then download from GitHub Releases.

Compatibility: HPE Morpheus 8.0.7 runtime; compiled against `morpheus-plugin-api` 1.2.8.