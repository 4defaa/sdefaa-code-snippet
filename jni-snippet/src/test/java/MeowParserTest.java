import com.sdefaa.jni.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Julius Wong
 * <p>
 * MeowParser的测试类
 * <p>
 * @since 1.0.0
 */
public class MeowParserTest {

    /**
     * 初始化
     */
    @Test
    public void testInit(){
        MeowParser.init();
    }

    /**
     * 销毁
     */
    @Test
    public void testDestroy(){
        MeowParser.destroy();
    }

    /**
     *  解析
     * @throws ParserException 解析异常
     */
    @Test
    public void testParse() throws ParserException {
        ParserContent parserContent = new ParserContent();
        parserContent.setContent(MeowType.MEOW.getContent());
        System.out.println( MeowParser.parse(parserContent).toString());
        parserContent.setContent(MeowType.SNORE.getContent());
        System.out.println( MeowParser.parse(parserContent).toString());
        parserContent.setContent(MeowType.CHATTER.getContent());
        System.out.println( MeowParser.parse(parserContent).toString());
        parserContent.setContent(MeowType.HOWL.getContent());
        System.out.println( MeowParser.parse(parserContent).toString());
        parserContent.setContent(MeowType.ROAR.getContent());
        System.out.println( MeowParser.parse(parserContent).toString());
        parserContent.setContent(MeowType.HISS.getContent());
        System.out.println( MeowParser.parse(parserContent).toString());
        parserContent.setContent(MeowType.NULL.getContent());
        System.out.println( MeowParser.parse(parserContent).toString());
    }

    /**
     * 参数parserContent为null抛出ParserException异常
     */
    @Test
    public void testParseException1(){
        Assertions.assertThrowsExactly(ParserException.class, () -> {
            ParserResult parserResult = MeowParser.parse(null);
        });
    }

    /**
     * ParserContent#content为null抛出ParserException异常
     */
    @Test
    public void testParseException2(){
        Assertions.assertThrowsExactly(ParserException.class, () -> {
            ParserContent parserContent = new ParserContent();
            ParserResult parserResult = MeowParser.parse(parserContent);
        });
    }
}
