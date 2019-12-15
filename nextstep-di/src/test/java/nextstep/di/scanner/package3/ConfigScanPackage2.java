package nextstep.di.scanner.package3;

import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;

@Configuration
@ComponentScan({"nextstep.di.scanner.package2"})
public class ConfigScanPackage2 {
}
