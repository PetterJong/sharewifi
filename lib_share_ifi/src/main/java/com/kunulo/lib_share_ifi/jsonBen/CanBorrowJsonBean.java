package com.kunulo.lib_share_ifi.jsonBen;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : xxx@xx
 *     time   : 2017/09/01
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class CanBorrowJsonBean extends Bean {
    private CanBorrowBeanValue value;

    public CanBorrowBeanValue getValue() {
        return value;
    }

    public void setValue(CanBorrowBeanValue value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PayDepositJsonBean{" +
                "value=" + value +
                '}';
    }

    private class CanBorrowBeanValue{
        private String isBorrow;

        public String getIsBorrow() {
            return isBorrow;
        }

        public void setIsBorrow(String isBorrow) {
            this.isBorrow = isBorrow;
        }

        @Override
        public String toString() {
            return "CanBorrowBeanValue{" +
                    "isBorrow='" + isBorrow + '\'' +
                    '}';
        }
    }
}
