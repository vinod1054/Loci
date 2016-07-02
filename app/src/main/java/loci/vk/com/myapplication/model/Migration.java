package loci.vk.com.myapplication.model;

/**
 * Created by vinod on 3/3/16.
 */

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Example of migrating a Realm file from version 0 (initial version) to its last version (version 3).
 */
public class Migration implements RealmMigration {

    @Override
    public void migrate(final DynamicRealm realm, long oldVersion, long newVersion) {
        // During a migration, a DynamicRealm is exposed. A DynamicRealm is an untyped variant of a normal Realm, but
        // with the same object creation and query capabilities.
        // A DynamicRealm uses Strings instead of Class references because the Classes might not even exist or have been
        // renamed.

        // Access the Realm schema in order to create, modify or delete classes and their fields.
        RealmSchema schema = realm.getSchema();

        /************************************************
         // Version 0
         class Person
         @Required
         String firstName;
         @Required
         String lastName;
         int    age;
         // Version 1
         class Person
         @Required
         String fullName;            // combine firstName and lastName into single field.
         int age;
         ************************************************/
        // Migrate from version 0 to version 1
        if (oldVersion == 0) {

            oldVersion++;
        }

        /************************************************
         // Version 2
         class Pet                   // add a new model class
         @Required
         String name;
         @Required
         String type;
         class Person
         @Required
         String fullName;
         int age;
         RealmList<Pet> pets;    // add an array property
         ************************************************/
        // Migrate from version 1 to version 2
        if (oldVersion == 2) {
            schema.get("MyLocation").addField("status1",int.class);

            oldVersion++;
        }

        /************************************************
         // Version 3
         class Pet
         @Required
         String name;
         int type;               // type becomes int
         class Person
         String fullName;        // fullName is nullable now
         RealmList<Pet> pets;    // age and pets re-ordered (no action needed)
         int age;
         ************************************************/
        // Migrate from version 2 to version 3

    }
}
