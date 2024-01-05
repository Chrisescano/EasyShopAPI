package org.yearup.data;


import org.yearup.models.Profile;

public interface ProfileDao
{
    Profile getByUserId(int userId);
    Profile edit(Profile profile);
    Profile create(Profile profile);
}
