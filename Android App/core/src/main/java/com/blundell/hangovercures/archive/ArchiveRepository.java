package com.blundell.hangovercures.archive;

import com.blundell.hangovercures.Cure;

import java.util.List;

interface ArchiveRepository {

    void signInUser(); //TODO UserRepository?

    User getUser();

    List<Cure> getCures();
}
