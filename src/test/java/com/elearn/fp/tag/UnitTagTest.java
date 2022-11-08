package com.elearn.fp.tag;

import com.elearn.fp.db.entity.Unit;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.util.*;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UnitTagTest {

    private UnitTag unitTag;
    private ServletContext servletContext;
    private List<Unit> unitList;

    private PageContext pageContext;
    private JspWriter writer;

    @BeforeAll
    void setUp() {
        servletContext = mock(ServletContext.class);
        unitList = new ArrayList<>(List.of(new Unit(1,"kg"),new Unit(2,"pcs")));
        pageContext = mock(PageContext.class);
        unitTag = mock(UnitTag.class);
        writer = mock(JspWriter.class);
    }

    @Test
    void daStartTag() throws JspException {
        when(pageContext.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("units")).thenReturn(unitList);
       // when(unitList.stream()).thenReturn(unitList.stream());
      //  Stream<Unit> stream = mock(Stream.class);
      //  when(unitList.stream()).thenReturn(stream);
      //  Stream<Unit> filtered = mock(Stream.class);
     //   when(stream.filter(u -> u.getId() == unitTag.getUnit())).thenReturn(filtered);
      //  Stream<String> names = mock(Stream.class);
      //  when(filtered.map(Unit::getName)).thenReturn(names);
     //   Optional<String> name = mock(Optional.class);
     //   when(names.findFirst()).thenReturn(name);
    //    when(name.get()).thenReturn("kg");


        unitTag.setUnit(1);
        unitTag.doStartTag();

    }

}
