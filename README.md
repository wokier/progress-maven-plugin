progress-maven-plugin
=====================

Maven Reactor Progress

---

Usage :

<pre>
	&lt;plugin&gt;
		&lt;groupId&gt;com.github.wokier.progress-maven-plugin&lt;/groupId&gt;
		&lt;artifactId&gt;progress-maven-plugin&lt;/artifactId&gt;
		&lt;version&gt;0.5&lt;/version&gt;
		&lt;executions&gt;
			&lt;execution&gt;
				&lt;goals&gt;
					&lt;goal&gt;display-progress&lt;/goal&gt;
					or
					&lt;goal&gt;notify-progress&lt;/goal&gt;
				&lt;/goals&gt;
			&lt;/execution&gt;
		&lt;/executions&gt;
	&lt;/plugin&gt;
</pre>

Release Notes :

 - 0.5 2013-08-30 Uses java-to-OS-notify v0.5
 - ~~0.4~~
 - 0.3 2013-04-20 Maven Central Release
