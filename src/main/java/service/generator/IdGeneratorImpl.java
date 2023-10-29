package service.generator;

public class IdGeneratorImpl implements IdGenerator {

    private static int current;

    @Override
    public int generate() {
        return ++current;
    }
}
