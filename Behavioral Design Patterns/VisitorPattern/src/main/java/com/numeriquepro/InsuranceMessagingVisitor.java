package com.numeriquepro;

final class InsuranceMessagingVisitor implements ClientVisitor {
    @Override public void visitResident(Resident r) {
        System.out.println("Email santé → " + r.name());
    }
    @Override public void visitBank(Bank b) {
        System.out.println("Email vol → " + b.name());
    }
}