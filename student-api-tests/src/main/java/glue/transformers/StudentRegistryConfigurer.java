package glue.transformers;

import com.blockone.test.studentapi.mdl.Student;
import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableEntryTransformer;

import java.util.Locale;

public class StudentRegistryConfigurer implements TypeRegistryConfigurer {

    @Override
    public Locale locale() {
        return Locale.ENGLISH;
    }

    @Override
    public void configureTypeRegistry(TypeRegistry typeRegistry) {
        TableEntryTransformer<Student> transformer = map -> Student.builder()
                .id(Long.valueOf(map.get("id")))
                .firstName(map.get("firstName"))
                .lastName(map.get("lastName"))
                .clazz(map.get("class"))
                .nationality(map.get("nationality")).build();
        DataTableType tableType = new DataTableType(Student.class, transformer);
        typeRegistry.defineDataTableType(tableType);
    }


}
