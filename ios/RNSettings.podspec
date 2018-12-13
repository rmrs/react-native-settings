require 'json'
version = JSON.parse(File.read('../package.json'))["version"]

Pod::Spec.new do |s|
  s.name         = "RNSettings"
  s.version      = version
  s.summary      = "RNSettings"
  s.description  = <<-DESC
                  Access iOS and Android device settings from React Native
                   DESC
  s.homepage     = "https://github.com/rmrs/react-native-settings"
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "erez@rumors.io" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/rmrs/react-native-settings.git", :tag => s.version.to_s }
  s.source_files  = "RNSettings/**/*.{h,m}"
  s.requires_arc = true


  s.dependency "React"
  #s.dependency "others"

end
