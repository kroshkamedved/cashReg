package com.elearn.fp.tag;

import com.elearn.fp.db.entity.Unit;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;

public class UnitTag extends TagSupport {
    private int unit;

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    @Override
    public int doStartTag() throws JspException {
        List<Unit> unitsList = (List<Unit>) pageContext.getServletContext().getAttribute("units");
        String unitName = unitsList.stream()
                .filter(u -> u.getId() == unit)
                .map(u -> u.getName())
                .findFirst()
                .get();
        try {
            pageContext.getOut().print(unitName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return SKIP_BODY;
    }


}
