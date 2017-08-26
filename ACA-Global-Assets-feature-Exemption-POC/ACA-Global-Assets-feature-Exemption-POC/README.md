# ACA-Global-Assets
Global assets for all ACA projects

##Document Services

###Reader Extensions Configuration
Before following the steps described in the AEM Forms 6.1 [documentation](https://helpx.adobe.com/aem-forms/6-1/configuring-document-services.html#Set%20up%20certificates%20for%20Reader%20extensions), the service account used for Reader Extensions must be created. Here are the steps required to create the account:

- Navigate to the CRX Explorer Console ([*host*:*port*/crx/explorer](localhost:4502/crx/explorer))
- Click on the "Create User" link
- Select the "Create System User" action
    - Enter a user ID (the default required ID is: **ares**. See *Modify user name* to use a different name.)
    - Select an intermediate path where the user node will be stored (this will help you locate the generated node: i.e. */home/users/system/ares*)
- Click the checkmark to save
- Navigate to the AEM User Admin ([*host*:*port*/useradmin](http://localhost:4502/useradmin))
- Locate the user in the list of users, and click on the user's name
- Open the *Permissions* tab
- Expand the folder view to drill down into the user's home directory (i.e. */home/users/system/ares*)
- Select the user node (*dynamically generated ID under the home directory*), and add the following permissions:
    - Read
    - Modify
    - Create
    - Delete
- Click Save
- You may now follow the [documentation](https://helpx.adobe.com/aem-forms/6-1/configuring-document-services.html#Set%20up%20certificates%20for%20Reader%20extensions) steps to add the Reader Extensions certificate to the user.

**Modify user name**

In order to use a user with a name other than **ares**, the service account mapping must first be manually edited following these steps:

- Navigate to the AEM Configuration Web Console ([*host*:*port*/system/console/configMgr](http://localhost:4502/system/console/configMgr))
- Locate the **Apache Sling Service User Mapper** service and create a new amendment using the [Apache Sling Service User Mapper Amendment](http://localhost:4502/system/console/configMgr/org.apache.sling.serviceusermapping.impl.ServiceUserMapperImpl.amended) service
- Add a new entry to the list of mappings user the following values:
    - gov.hhs.cms.aca.global-assets.core:*useralias*=*username*
- Click OK to save
- Next, configure the **ACA Global Assets - Doc Assurance Service Caller** service with the following values:
    - In the *Reader Extensions: Credential User Account* field, enter the alias of the new user mapping created above
    - You may also specify a new Credential Alias in the *Reader Extensions: Credential Alias* field if you know the name of the alias used
- Click OK to save.
- You may now configure the Reader Extensions Certificate for the user name specified in the new service user mapping.