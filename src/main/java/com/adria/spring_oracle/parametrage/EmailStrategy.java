package com.adria.spring_oracle.parametrage;

import com.adria.spring_oracle.dao.BankTransaction;
import com.adria.spring_oracle.dao.ParamEmail;

public interface EmailStrategy {
    ParamEmail createParamEmail(BankTransaction transaction);
}
