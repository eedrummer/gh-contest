My attempt at the GitHub Contest
================================

This code represents my attempt at the GitHub contest. I took the contest as a chance to play with some technologies
that I have had my eye on. Namely, I wanted to write some code in [Scala](http://www.scala-lang.org/) and I also 
wanted to use [Tokyo Cabinet](http://tokyocabinet.sourceforge.net/). I'm posting the code so that people can have 
some examples of using Tokyo Cabinet with Scala. I didn't find many on the interwebs.

The Theory
----------

I thought a simple algorithm for guessing repositories in the contest would be to find the most popular repositories
and if a user had not subscribed to them already, guess them.

In Practice
-----------

This doesn't work out all that well. It also turns out that many other people had the same idea, and had even generated
identical results.txt files. I would up adding a randomizer in my code just so that I would have a different results.txt
so that I could at least show on the leaderboard.

Implementation
--------------

I used the Java API for Tokyo Cabinet, since it is easy to use Java libraries in Scala. I also wanted to serialize
Scala objects into Tokyo Cabinet, and for this I used [sbinary](http://github.com/DRMacIver/sbinary/tree/master).

Running
-------

I wrote the code using the [Scala Plugin for Eclipse](http://www.scala-lang.org/node/94). I just used the Eclipse
environment for compiling and running the code, so I don't have any specific build files.

If you do want to run the code, you should run the following:

1. parser.Slurp - This pulls in data.txt file and creates a hash with users as a key and a list of their watched repos at the value
2. parser.RepoSlurp - This also pulls in the data.txt file but it creates a different hash with the repo id as the key and the count of watchers as the value
3. parser.RepoTableSlurp - Pulls in the repos.txt file and uses the repo hash to create a table database describing the repos
4. parser.ResultsGenerator - Uses the databases to generate the results file

The code should be relatively straight forward, and each of the parser files explains what it does in its comments.

License
-------

Copyright 2009 Andrew Gregorowicz

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

