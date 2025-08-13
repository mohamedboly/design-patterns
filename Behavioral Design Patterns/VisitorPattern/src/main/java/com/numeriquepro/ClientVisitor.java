package com.numeriquepro;

interface ClientVisitor {
    void visitResident(Resident resident);
    void visitBank(Bank bank);
}