package nextstep.di.factory.scanner;

import nextstep.di.factory.beans.BeanRecipe;

import java.util.Set;

public interface BeanScanner {
    Set<BeanRecipe> scan();
}
