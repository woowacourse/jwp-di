package nextstep.di.scanner.package2;

import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"nextstep.di.scanner.package1"})
public class ConfigScanPackage1 {
}
