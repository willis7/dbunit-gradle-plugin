package com.ferigma.gradle.dbunit

import com.ferigma.gradle.dbunit.tasks.AbstractDbunitTask
import org.gradle.api.DomainObjectCollection
import org.gradle.api.Plugin
import org.gradle.api.Project

import com.ferigma.gradle.dbunit.tasks.CompareTask
import com.ferigma.gradle.dbunit.tasks.ExportTask
import com.ferigma.gradle.dbunit.tasks.OperationTask

/**
 * The DbUnit Gradle Plugin allows you to easily launch DbUnit tasks from your
 * gradle project. It is based on the DbUnit Maven Plugin to provide the same
 * (and more) functionality but simplifying the configuration process.
 *
 * <p><b>Tasks</b></p>
 * <p>There are three tasks included in the plugin:</p>
 * <ul>
 * <li><b>operation</b>: Allows to specify a list of sources to operate against
 * the database. Each source contains the file path with the data and the database operation. Available operations are INSERT, CLEAN, CLEAN_INSERT, DELETE, ...</li>
 * <li><b>compare</b>: Compares a file against the current database content.</li>
 * <li><b>export</b>: Exports the current database content to file.</li>
 * </ul>
 *
 * @author Fernando Iglesias Martinez
 * @author Sion Williams
 * @see http://dbunit.sourceforge.net
 * @see http://mojo.codehaus.org/dbunit-maven-plugin
 * @see http://github.com/ferigma/dbunit-gradle-plugin
 */
class DbunitGradlePlugin implements Plugin<Project> {

   /** Plugin group name. */
   public static final String PLUGIN_GROUP = "DbUnit"

   /**
    * {@inheritDoc}
    */
   @Override
   void apply(Project target) {

      // Project Description
      target.description = "DbUnit Gradle Plugin"

      registerDbunitExtensions(target)
      applyConventionMappingToDbunitTasks(target)
      registerTasks(target)

   }

   private DbunitPluginExtension registerDbunitExtensions(Project target) {
      target.extensions.create('dbunit', DbunitPluginExtension)
   }

   /**
    * assign the values from the extension object properties to the task properties with a closure.
    * This means the value of the properties are lazy set: only when the task gets executed the task
    * property values are calculated.
    * @param target
     */
   private void applyConventionMappingToDbunitTasks(Project target) {
      def dbunitExtension = target.extensions.findByName('dbunit')

      target.tasks.withType(AbstractDbunitTask) {
         conventionMapping.driver = { dbunitExtension.driver }
         conventionMapping.username = { dbunitExtension.username }
         conventionMapping.password = { dbunitExtension.password }
         conventionMapping.url = { dbunitExtension.url }
         conventionMapping.schema = { dbunitExtension.schema }
         conventionMapping.dataTypeFactoryName = { dbunitExtension.dataTypeFactoryName }
         conventionMapping.supportBatchStatement = { dbunitExtension.supportBatchStatement }
         conventionMapping.useQualifiedTableNames = { dbunitExtension.useQualifiedTableNames }
         conventionMapping.datatypeWarning = { dbunitExtension.datatypeWarning }
         conventionMapping.escapePattern = { dbunitExtension.escapePattern }
         conventionMapping.skipOracleRecycleBinTables = { dbunitExtension.skipOracleRecycleBinTables }
         conventionMapping.skip = { dbunitExtension.skip }
         conventionMapping.metadataHandlerName = { dbunitExtension.metadataHandlerName }
         conventionMapping.caseSensitiveTableNames = { dbunitExtension.caseSensitiveTableNames }
      }
   }

   private void registerTasks(Project target) {
      target.task('compare', type: CompareTask)
      target.task('export', type: ExportTask)
      target.task('operation', type: OperationTask)
   }
}