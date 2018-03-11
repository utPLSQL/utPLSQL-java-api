package org.utplsql.api.reporter;

import java.math.BigDecimal;

public class DocumentationReporter extends DefaultReporter {

    private int lvl;
    private int failed;

    public DocumentationReporter() {
        super( CoreReporters.UT_DOCUMENTATION_REPORTER.name(), null );
    }

    public DocumentationReporter(String selfType, Object[] attributes ) {
        super(selfType, attributes);
    }

    @Override
    protected void setAttributes(Object[] attributes) {
        super.setAttributes(attributes);

        if ( attributes != null ) {
            lvl = ((BigDecimal)attributes[3]).intValue();
            failed = ((BigDecimal)attributes[4]).intValue();
        }
    }

    @Override
    protected Object[] getAttributes() {
        Object[] attributes = super.getAttributes();
        attributes[3] = lvl;
        attributes[4] = failed;
        return attributes;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

}
