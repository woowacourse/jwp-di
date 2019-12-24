package nextstep.di.factory;

import nextstep.di.factory.scanner.ClasspathBeanScanner;
import nextstep.di.factory.scanner.ConfigurationBeanScanner;

abstract class AbstractBeanScannerTest {
    private static final String PATH = "nextstep.di.factory.example.";

    ClasspathBeanScanner cbs;
    ConfigurationBeanScanner cbds;

    void initClasspathBeanScanner() {
        this.cbs = new ClasspathBeanScanner(PATH);
    }

    void initConfigurationpathBeanScanner() {
        this.cbds = new ConfigurationBeanScanner(PATH);
    }

    void initAllBeanScanner() {
        this.cbs = new ClasspathBeanScanner(PATH);
        this.cbds = new ConfigurationBeanScanner(PATH);
    }
}
