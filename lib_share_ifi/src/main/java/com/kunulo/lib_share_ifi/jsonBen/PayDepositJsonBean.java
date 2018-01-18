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
public class PayDepositJsonBean extends Bean {
    private PayDepositBeanValue value;

    public PayDepositBeanValue getValue() {
        return value;
    }

    public void setValue(PayDepositBeanValue value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PayDepositJsonBean{" +
                "value=" + value +
                '}';
    }

    private class PayDepositBeanValue{
        private String havingDeposit;

        public String getHavingDeposit() {
            return havingDeposit;
        }

        public void setHavingDeposit(String havingDeposit) {
            this.havingDeposit = havingDeposit;
        }

        @Override
        public String toString() {
            return "PayDepositBeanValue{" +
                    "havingDeposit='" + havingDeposit + '\'' +
                    '}';
        }
    }

}
