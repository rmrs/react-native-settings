
Pod::Spec.new do |s|
  s.name         = "RNSettings"
  s.version      = "0.0.1"
  s.summary      = "RNSettings"
  s.description  = <<-DESC
                  Access iOS and Android device settings from React Native
                   DESC
  s.homepage     = "https://github.com/rmrs/react-native-settings"
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "erez@rumors.io" }
  s.platform     = :ios, "9.0"
  s.source       = { :git => "https://github.com/rmrs/react-native-settings.git", :tag => "v0.0.1-alpha7" }
  s.source_files  = "RNSettings/**/*.{h,m}"
  s.requires_arc = true


  s.dependency "React"
end
