package ru.sgu.csit.inoc.deansoffice.webui.gxt.students.server.utils;

import ru.sgu.csit.inoc.deansoffice.domain.Dean;
import ru.sgu.csit.inoc.deansoffice.domain.Faculty;
import ru.sgu.csit.inoc.deansoffice.webui.gxt.students.shared.model.FacultyModel;
import ru.sgu.csit.inoc.deansoffice.webui.gxt.students.shared.model.PersonModel;

/**
 * User: Khurtin Denis ( KhurtinDN (a) gmail.com )
 * Date: 2/11/11
 * Time: 4:08 PM
 */
public class FacultyUtil {
    public static FacultyModel convertFacultyToFacultyModel(Faculty faculty) {
        if (faculty == null) {
            return null;
        }

        Dean dean = faculty.getDean();
        PersonModel deanModel = new PersonModel();
        PersonUtil.populatePersonModelByPerson(deanModel, dean);

        FacultyModel facultyModel =  new FacultyModel(faculty.getId());
        facultyModel.setDean(deanModel);
        facultyModel.setName(faculty.getShortName());
        facultyModel.setFullName(faculty.getFullName());
        facultyModel.setCourseCount(faculty.getCourseCount());

        return facultyModel;
    }
}
