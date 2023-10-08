package ru.avkalabin;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Практика с параметризированными тестами")
public class ParameterizedTests {
    @BeforeEach
    void beforeEach() {
        Configuration.browserSize = "1920x1080";
        Configuration.pageLoadStrategy = "eager";
    }

    @DisplayName("Проверка поиска на GitHub")
    @ParameterizedTest(name = "В выдаче результатов на первом месте должен отображаться {0}")
    @ValueSource(strings = {"junit5", "selenide"})
    void firstSearchResultShouldContainExpectedTest(String testData) {
        open("https://github.com/");
        $("[placeholder='Search or jump to...']").click();
        $("#query-builder-test").setValue(testData).pressEnter();
        $$("[data-testid='results-list'] div").first().shouldHave(text(testData));

    }


    @DisplayName("Проверка регистрации на demoqa.com")
    @ParameterizedTest(name = "В поле вывода результатов регистрации должны отображаться {0}, {1}, {2}, {3}")
    @CsvSource({
            "Alex Egorov, alex@egorov.com, Some address 1, Another address 1",
            "Ivan Semenov, ivan@semenov.com, Some address 3, Another address 4"
    })
    void fillFormTest(String testName, String testEmail, String currentAddress, String permanentAddress) {
        open("https://demoqa.com/text-box");

        $("#userName").setValue(testName);
        $("#userEmail").setValue(testEmail);
        $("#currentAddress").setValue(currentAddress);
        $("#permanentAddress").setValue(permanentAddress);
        $("#submit").click();

        $("#output").shouldHave(text(testName), text(testEmail),
                text(currentAddress), text(permanentAddress));
    }


    static Stream<Arguments> uploadPicture() {
        return Stream.of(
                Arguments.of(new File("src/test/resources/images/sampleFile.jpeg"), "sampleFile.jpeg"),
                Arguments.of(new File("src/test/resources/images/luminoslogo.png"), "luminoslogo.png")
        );
    }

    @DisplayName("Проверка загрузки файла на demoqa.com")
    @ParameterizedTest(name = "При загрузке файла {0} в пути загруженного файла должно отображаться {1}")

    @MethodSource()
    void uploadPicture(File file, String fileName) {
        open("https://demoqa.com/upload-download");

        $("#uploadFile").uploadFile(file);

        $("#uploadedFilePath").shouldHave(text(fileName));

    }

}
