Pod::Spec.new do |spec|
    spec.name                     = 'connectivity_status'
    spec.version                  = '1.0'
    spec.homepage                 = 'https://javokhirsavriev.github.io/'
    spec.source                   = { :http=> ''}
    spec.authors                  = ''
    spec.license                  = ''
    spec.summary                  = 'KMP library to monitor the connectivity status of the device. Supports Android and iOS.'
    spec.vendored_frameworks      = 'build/cocoapods/framework/connectivity_status.framework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target = '14.1'
    spec.dependency 'Reachability', '3.2'
                
    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':connectivity-status',
        'PRODUCT_MODULE_NAME' => 'connectivity_status',
    }
                
    spec.script_phases = [
        {
            :name => 'Build connectivity_status',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED" ]; then
                  echo "Skipping Gradle build task invocation due to OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED environment variable set to \"YES\""
                  exit 0
                fi
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
                    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
                    -Pkotlin.native.cocoapods.archs="$ARCHS" \
                    -Pkotlin.native.cocoapods.configuration="$CONFIGURATION"
            SCRIPT
        }
    ]
                
end