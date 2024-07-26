package com.adria.spring_oracle.parametrage;

import com.adria.spring_oracle.dao.BankTransaction;
import com.adria.spring_oracle.dao.ParamSMS;

public interface SmsStrategy {
    ParamSMS createParamSMS(BankTransaction transaction);
}
