~ number: 0
~ title: Being a Good Classmate and A Few Java Exercises

[online_compile]: http://www.tutorialspoint.com/compile_java8_online.php
[textbook]: https://inst.eecs.berkeley.edu/~cs61b/sp20/materials/book1/java.pdf

Navigation
----

- [Preliminaries](#a-preliminaries)
- [Respectful Communication](#b-respectful-communication)
- [MAX](#c-max)
- [3SUM](#d-3sum)
- [3SUM_DISTINCT](#e-3sum_distinct)
- [FAQ](#f-faq)
- [Submission](#g-submission)
- [Getting Help](#h-getting_help)


A. Preliminaries
----

### Goals

There are three main subparts to this homework: a Gradescope assignment, the [pre-semester survey](https://docs.google.com/forms/d/e/1FAIpQLSda9hY3HjG0hqjNeD8N6oObOUDNT1sDXA7DlWYQohxECchRSQ/viewform), and a few coding exercises.
1. The Gradescope assignment will prime you for the rest of this class, and help establish one of the key principles of being a software engineer: how to communicate. The Gradescope assignment is available and titled "Homework 0 Part 1: Respectful Communication", and it repeats all of the content in the spec under section B "Respectful Communication" for your convenience. This assignment is worth 1 point and will be graded based off of your honest effort and if you have demonstrated understanding of the material.
2. The pre-semester survey will help us gather some important information that will inform how the rest of the semester plays out. This is worth 1 point.
3. The coding exercises here are ungraded and aim to introduce you to a few basic points of Java
syntax:
  - Initializing variables, e.g. `int x = 5;`
  - Comparisons and logical operators
  - Conditionals (if statements)
  - Iteration (using for and while)

  You won't be graded on whether or not the code works as it should, but with that said we suggest that you do give them a serious try in order to give yourself a chance to get comfortable with Java and dust off your programming skills from last semester.

In total, the homework assignment is worth 2 points.

For each of the coding problems,
* No starter code is provided.
  Use examples from class and the textbook to write your solutions.
  We don't care what you call your classes, since we don't intend to test
  your submissions.
* You can use whatever tool you'd like to write and run your code.
  This could be the javac/java on your own computer or the lab machines, etc.,
  or IntelliJ,
  or an [online execution tool][online_compile].
* For each problem, use the main method to perform an ad-hoc test
  (by ad-hoc, I just mean a crude test that isn't generalizable
  like the fancier tests that you'll be developing in HW1). For
  example, you can write in your main method a few print statements
  calling your method to ensure it correctly prints the right thing.

B. Respectful Communication
----

### Motivation

In CS 61B and throughout your time at Berkeley, you will learn many skills that will make you a good programmer. CS 61B is often regarded as the first class at Berkeley that really teaches about software engineering. When you hear this, you probably think this is entirely referring to the fact that this class is known for its large projects with little scaffolding. However, software engineering is a lot more than just churning out code like a machine–a real software engineer is typically someone who works with others, faces deadlines and stress, and has to manage it while maintaining respectful communication. Thus, in this class we consider it our duty to also teach effective communication, so that when you approach real world projects you can convey your ideas to colleagues. Even if you have excellent ideas and can design complex algorithms, a lack of ability to relay them to others will hinder your professional development. As such, this portion of the homework exists to get you thinking on how to interact with a wide array of individuals.

Respectful collaboration is especially vital when working with those generally underrepresented in computer science. For example, only 20-30% of Berkeley EECS/CS students use she/her/hers pronouns. Fewer than 1% of EECS/CS students identify as Black. As a result, it can feel daunting to members of these and other underrepresented communities to attend our classes. Often, when we think of discrimmination towards these groups we think of extreme acts such as sexual harassment or explicit hate speech. However, the small, frequent instances of negative remarks make an equally big impact. While the perpetrators may only participate in these situations rarely, the imbalance between the raw number of potential offenders and minority students means minority students face these situations constantly.

**This assignment was created for an important reason - underrepresented groups continue to face discrimination actively throughout computing, including when taking CS 61B**. Examples from the TAs’ experiences include being excluded from groups during lab, being sexually harassed by other students during class, and being told they only got opportunities due to their identity. It is ok to make mistakes; in reality, most people have misstepped in a social situation at some point or another. What matters is how you handle these uncomfortable situations once they occur. **We ask you to genuinely read this homework and consider your actions so that CS 61B can be as welcoming and inclusive as possible**. We understand that all students will have varying levels of familiarity with this material. If you are lost, we encourage you to do more research (we have more links in the FAQ). If you feel that the assignment is too elementary, we apologize but hope you can appreciate that our goal is to make sure everyone has the same baseline understanding.

Some statistics that help paint a picture of the impact of disrespectful communication:
  - 40% of female students and 45% of non-binary students feel as though they need to prove themselves before being taken seriously in academic settings, compared to 25% of male students
  - 49% of students with disabilities have considered leaving computer science, compared to 28% of students without disabilities
  - 12% of low-income students have been encouraged to drop out by a faculty member, compared to 3% of higher income students.
  - 51% of Black students and 73% of Hispanic students have been told their racial identity unfairly advantaged them getting job opportunities, compared to 7% of Asian students and 10% of White students
  - 22% of non-heterosexual students believe all students have equal opportunities to succeed, compared to 37% of heterosexual students

### What Does Disrespectful Communication Look Like?

Discrimination comes in many forms, both big and small. Certain statements may disproportionately affect some people more than others. Everyone has aspects of their history that they are sensitive about and in order to make sure everyone is comfortable, students should strive to avoid these sensitive topics. Because of this, we encourage students to stay professional in the classroom setting and never assume someone else’s comfort level with inappropriate humor.

Additionally, we often joke around with friends in a way that is not appropriate for professional settings because we know our friends and can give them the benefit of the doubt. While someone may be comfortable with their friends making insensitive jokes or comments, they may find it uncomfortable when told by someone they aren’t familiar with.
> *Think this information isn't really relevant?* One of our TAs can provide an example of a time someone else's humor made her uncomfortable: this TA was the only non-cisgender male in a group chat meant for classwork, but another student sent a crass sexual video as a joke. While this TA may or may not be comfortable with crass humor in other contexts, this scenario was uncomfortable because she couldn’t easily leave the assigned group, meaning she had to either ignore her discomfort or face an awkward conversation with people she wasn’t close to.

Examples of disrespectful communication include:
  - **Asking inappropriate questions**
    Inappropriate questions come in many forms, including asking a student to explain an aspect of their identity that is not relevant to the course material. This includes things like asking probing questions about race, ethnicity, gender or disability.
  - **Making assumptions about people’s experiences or backgrounds**
    Assuming aspects of people’s backgrounds - both positive and negative - serves to reinforce stereotypes. Do not make assumptions about others’ identities or academic experiences.
  - **Using a condescending or patronizing tone**
    Talking down towards others is unacceptable. If you cannot work respectfully with another student, you should remove yourself from the situation rather than treating them poorly. Everyone deserves to be treated with respect, with no exceptions.
  - **Avoiding partnerships on the basis of belonging to an underrepresented group**
    This is an unfortunately common situation, faced most significantly by members of underrepresented racial or ethnic groups. We expect all students in CS 61B to be welcoming towards others in lab, discussion, and office hours. This includes both responding with kindness when someone asks to be included in a group, but also actively reaching out if someone is sitting alone.
  - **Asking about grades or scores on assignments**
    Grades and scores are a form of personal information that each student is entitled to keep private. Due to the competitive nature of computer science classes, we discourage students from sharing or asking about grades or scores to foster a kinder environment.


### How Do I Respond if I’m in an Uncomfortable Situation?

When dealing with an uncomfortable situation, you may be in one of three roles: the person who has just been hurt, the person who has hurt someone, or a bystander. Most people have been in all of these roles at one point or another - what is important once you find yourself in an uncomfortable situation you react in an appropriate and timely manner.

#### What to do if you’ve hurt someone:
Sometimes, things you say might come out wrong or cause harm even if you have good intentions. If you find yourself in a situation where you’ve negatively impacted someone with your words or actions, there are a few steps you can take to step back and reduce the harm caused by your misstep:

- **Make it about them, not you**
  Usually, your first instinct is to explain what you meant, and how you aren’t a horrible person. However, in the moment, what is more important is to make the person who was hurt feel better. After all, if you accidentally step on someone’s foot, you should not expect them to make you feel better for stepping on their foot.
- **Apologize**
  Verbally apologize immediately and sincerely, without drawing back to anyone else’s behavior. Verbalizing your transgression goes a long way both in making you self-reflect and letting the other person know you understand what you did wrong. Spending too much time on your apology shifts focus to you, and shifts the burden to the other person to validate you. Do not expect the person you’ve hurt to accept immediately - it places an unfair expectation on them to glaze over your behavior.
- **Amend your behavior**
  In the future, make an active effort to not repeat your mistakes.

#### What to do if you’ve just been hurt:
If you find yourself on the receiving end of disrespectful behavior, whether intentional or not, there are a few steps you can take if you wish, depending on the situation and your level of comfort with confrontation:

- **Make Yourself Feel Comfortable**
  It is okay if you prefer not to take action against inappropriate behavior - the priority here is ensuring that you feel comfortable. If that involves removing yourself from the situation or simply moving past it in the moment, that is up to your discretion.
- **Establish boundaries**
  Verbally express your feelings surrounding what was said to you and set expectations for future interactions.
- **Ask a TA to step in**
  Pull a TA aside and mention the behavior of your classmate. Ask the TA to step in on your behalf.
- **Report the incident**
  Submit the confidential incident form found on the website. Don’t hesitate even if you feel like the incident was “too small” and you’re worried about another student getting harshly punished because of your report. Action will only be taken through this if there is either an egregious act of inappropriate behavior, or if there is a repeated pattern of behavior by one student. This form will only be viewable to Professor Hilfinger and the head TAs (Henry, Linda, and Anjali).

#### What to do when you're a bystander:
You may find yourself in a conversation where one person acts disrespectfully towards another. Oftentimes, someone who has just been hurt may not speak up due to social pressure or fear of further judgement. As such, there are steps you can take to stand up for others:
- **Shut down the offender**
  Quickly and respectfully shut down the student who has made a harmful comment without putting the hurt student on the spot and escalating the situation. The offender may not have known they made a mistake, and in that case, they will respond best if you attempt to [call in, rather than call out](http://www.racialequityvtnea.org/wp-content/uploads/2018/09/Interrupting-Bias_-Calling-Out-vs.-Calling-In-REVISED-Aug-2018-1.pdf).
- **Pull the hurt person aside**
  After the conversation ends, ensure everyone is feeling okay if you feel comfortable doing so. A small check-in makes sure the person who was hurt sees that others have seen the disrespectful behavior and agree that it is not okay.
- **Make it about them, not you**
  Remember that this is about making sure they feel safe and comfortable, and is not an opportunity for you to show off how much you are aware of these situations. There’s no need to press the issue if they say they are okay (you don’t want to create a situation if none existed in the first place). Lastly, it’s entirely possible that the interaction makes you more uncomfortable than the victim. In this case, you should deal with the situation, separately, without involving them further.

### Respectful Communication: Conclusion
  Computer science is for everyone, regardless of background or identity. We believe that students generally have positive intentions, but there will always be situations where people say something inappropriate or make each other uncomfortable. Our goal is to minimize these inappropriate interactions and the harm they cause so that all students feel welcome while taking CS 61B and it is everyone's responsibility to ensure the class stays this way. **Now that you have completed this assignment, it is the expectation that we will all uphold these standards and treat each other with kindness and respect.**

Coding Assignment
----

### Setup for Coding
As with lab1, start this homework by using the following commands in your
local `repo` directory (assuming that you are using our normal setup--working
on the default `master` branch--and that you have committed any files you
you already have there from other assignments.)

    git fetch shared
    git merge shared/hw0 -m "Get HW0 skeleton"
    git push

This time there will be no skeleton file for your solution. The idea
is for you create and add the necessary files.  As you create new files, be sure
to use `git add` to add bring them under Git's control.  
We have provided a skeleton `Tester.java` file for writing simple tests of your
solutions.  However, you will have to figure out how to get it to actually
call your solutions before you can compile and run it.
You can use `javac` to compile it, and `java Tester` to run it.  

To submit, you will tag a specific commit. Details can be found in the [submission](#f-submission) section.

### Do the Reading

Make sure you've done the first reading assignment: AJR 1.1 through 1.9 ([found here][textbook]) and Chapter 1
of Head First Java. If you don't have Head First Java, that's fine,
AJR provides all the information you need (but might be a harder
read).

While you're reading, you may find it helpful to be able to run Java code so that you can experiment. You can compile and execute as you have done in lab,
of course.  There is also a convenient
[online Java compilation tool][online_compile].
To run code, simply add the code you'd like to execute to the `main`
method and then press the Compile and Execute buttons.

Head First Java provides you with exercises that you can do as you
read.  Feel free to skip doing these if you'd like.

C. Max
----

Write a function `max(int[] a)` that takes in a non-empty array (that is, size(a)>0) and returns the maximum value
of the array. Try writing this function using both a while loop
and a for loop.

A note about convenient syntax for creating arrays:

> You can specify an array using curly braces. A strange choice of
> symbols, for a Python programmer, but we're in the C family of languages
> here. For
> example:
>     int[] a = new int[]{1, 2, 3, 4}
> would create an array
> containing 1, 2, 3, and 4 and then give it the name a.

> In this context, the curly braces are interpreted in a completely
> different way from when we use them to begin and end a block of code.

To test, modify your main function to perform an ad-hoc test of your method. In this context, this means a non-generalizable test in which you feed in specific test cases as input to the function, compile, and check that the output is what is expected.

D. 3SUM
----

Suppose we have a non-empty array of integers `int[] a`.
The [3SUM problem](http://en.wikipedia.org/wiki/3SUM) asks if
there are three integers (not neccesarily distinct) in `a[]` whose sum is zero. You may assume that the array is not empty.

For this problem, write a function `threeSum(int[] a)` that returns true if there exist three integers in a that sum to zero and false otherwise. Integers may be used more than once. As in part 1, use your main function to perform an ad-hoc test that `threeSum` works.

For loops will look a lot more compact than while loops for this problem.

#####Examples:
- `threeSum(new int[]{-6, 2, 4})` is true, can select -6, 2, and 4.
- `threeSum(new int[]{-6, 2, 5})` is false.
- `threeSum(new int[]{-6, 3, 10, 200})` is true, can select -6, 3, and 3.
- `threeSum(new int[]{8, 2, -1, 15})` is true, can select 2, -1, and -1.
- `threeSum(new int[]{8, 2, -1, -1, 15})` is true, can select 2, -1, -1.
- `threeSum(new int[]{5, 1, 0, 3, 6})` is true, can select 0, 0, and 0.

This might seem daunting at first, but it's relatively
straightforward. As a hint, consider an
alternative way of stating the problem: Do there exist three indices
(not necessarily distinct) f, g, and h such that a[f] + a[g] + a[h] ==
0?

E. 3SUM_DISTINCT
----

Repeat the exercise from Part C, but with the constraint that each
element in the array can be used only once. Where in the last problem we wanted the three not necessarily distinct indices, we here require three distinct indices. Numbers can appear more than once in our sum if and only if they appear more than once in the given array. Again, you may assume the array is not empty.

#####Examples:
- `threeSumDistinct(new int[]{-6, 2, 4})` is true, can select -6, 2, and 4.
- `threeSumDistinct(new int[]{-6, 2, 5})` is false.
- `threeSumDistinct(new int[]{-6, 3, 10, 200})` is false.
- `threeSumDistinct(new int[]{8, 2, -1, 15})` is false.
- `threeSumDistinct(new int[]{8, 2, -1, -1, 15})` is true, can select 2, -1, and -1.
- `threeSumDistinct(new int[]{5, 1, 0, 3, 6})` is false.

F. FAQ
----

### Respectful Communication FAQs:
####1. If I’m dealing with these issues in my life right now, where can I go to get help or report others?
- Mental health: https://uhs.berkeley.edu/caps
- Trans healthcare: https://uhs.berkeley.edu/transgender-care
- Black healthcare: https://uhs.berkeley.edu/blackhealthmatters
- LGBTQIA+ Counseling: https://uhs.berkeley.edu/pride/counseling
- Group counseling for identity based groups: https://uhs.berkeley.edu/counseling/group
- Independent mental health support: https://www.thetrevorproject.org/
- NATIONAL SUICIDE PREVENTION LIFELINE: The National Suicide Prevention Lifeline offers services to talk to a skilled counselor. If you are feeling distressed and need to talk to a counselor, please call 1-800-273-TALK (8255).
If you prefer to chat to a counselor online, the National Suicide Prevention Lifeline offers that service. Click [here](http://suicidepreventionlifeline.org/GetHelp/LifelineChat.aspx) to chat online with a trained counselor.
- UC Berkeley’s Sexual Violence and Harassment policies and resources: https://svsh.berkeley.edu/
- We have an incident report form that is confidential here. It is only viewable by the professor and the head TAs (Anjali, Henry, and Linda), and we will keep your identity safe. You should not hesitate to report any discriminatory behavior, even if it’s minor–we will use our best judgment and only take action if we witness a severe or continued pattern of negative behavior towards other students.

####2. If I want to learn more, where can I go to read about these issues?
- Microaggressions
  - https://www.npr.org/2020/06/08/872371063/microaggressions-are-a-big-deal-how-to-talk-them-out-and-when-to-walk-away
  - Examples of racial microaggressions: https://sph.umn.edu/site/docs/hewg/microaggressions.pdf
  - What to do when you make a mistake: https://www.apa.org/monitor/2017/01/microaggressions and https://hbr.org/2020/07/youve-been-called-out-for-a-microaggression-what-do-you-do
- Cultural competency and respect:
  - https://extensionpublications.unl.edu/assets/html/g1375/build/g1375.htm
  - https://www.cs.utexas.edu/users/downing/papers/When-Twice-as-Good-Isnt-Enough-2020.pdf
  - https://www.idra.org/resource-center/religion-equity-in-schools-protecting-students-and-their-civil-rights/
- Disability
  - Etiquitte: https://www.bestcolleges.com/resources/disability-etiquette/
  - Current status: https://cra.org/crn/2020/11/expanding-the-pipeline-the-status-of-persons-with-disabilities-in-the-computer-science-pipeline/
- Harassment
  - https://svsh.berkeley.edu/community-prevention-response-efforts
  - https://svsh.berkeley.edu/support-resources
  - Title nine: https://www2.ed.gov/about/offices/list/ocr/docs/tix_dis.html
  - https://www.newamerica.org/political-reform/reports/misogynist-incels-and-male-supremacism/recommendations/
  - http://oro.open.ac.uk/61128/1/WebScience139.pdf
  - https://xyonline.net/sites/xyonline.net/files/2018-05/Ging,%20Alphas,%20Betas,%20and%20Incels%202017.pdf
- Queer and trans
  - https://www.glaad.org/sites/default/files/allys-guide-to-terminology_1.pdf
  - https://www.aclu.org/blog/lgbtq-rights/lgbtq-nondiscrimination-protections/its-always-been-about-discrimination-lgbt
  - https://www.lgbtmap.org/file/talking-about-overall-approaches-for-lgbt-issues.pdf
- Student parents
  - https://studentparents.berkeley.edu/

####3. Why do we have to learn about this in a computer science class?

This is a serious issue that exists at all levels of computer science. We do not want to send you all out into the workforce post-Berkeley if you will contribute to these harmful environments. If you want to read about a real world scenario of discrimination in computer science, you can read [this article](https://news.bloomberglaw.com/daily-labor-report/activision-blizzard-sued-by-california-over-frat-boy-culture). **Trigger warning**: Extreme sexual harassment, self harm, death.

### Coding FAQs:

####1. I am really lost on 3SUM? How do I start?
Consider first how you would handle 1SUM. How would you figure out if there is a specific index in the given array which is equal to 0? From there, think about how you would implement 2SUM. How would you find out if there exists two indices in the array j,k (not necessarily distinct, that is j can equal k) such that array[j] + array[k] = 0? From here, try to generalize for the number of terms you are summing over! You'll see that 3SUM, 4SUM, 5SUM, etc are all very similar and you could easily make any of them!

####2. How many test cases do I have to write?
You should think through the basic cases and as many of the corner cases as you can think of. Though you are not being graded on the correctness of your code for this assignment, later assignments will be graded on correctness and require you to test your own code! It is a good idea to start practicing now.

####3. I am getting a weird compiler error!! Help!
Before you post on Piazza, try googling the error you are getting. This is how you learn, Google is one of the most important tools for any person who writes software. For example, if your terminal says that you are getting an "IndexOutOfBoundsError", try typing that into Google and following the advice of a reputable website (for example, stackOverflow).

####4. My main method can't find my classes!
[Can't access method from main](https://stackoverflow.com/questions/45678971/cant-access-functions-in-another-class)

####5. I am getting issues with multiple methods of the same name!
Check out [this](https://docs.oracle.com/javase/tutorial/java/package/namingpkgs.html) documentation!

G. Submission
----
We will only be grading the Gradescope assignment and checking that you've filled out the [presemester survey](https://docs.google.com/forms/d/e/1FAIpQLSda9hY3HjG0hqjNeD8N6oObOUDNT1sDXA7DlWYQohxECchRSQ/viewform). If you haven't done all of these things, then you will not receive full credit for this homework. There is no autograder for the coding exercises, though you may test your code locally by writing some tests.

H. Getting Help
----

For future assignments we are using an infrastructure called Git Bugs for interfacing with staff for debugging help. More info to come on that in the next homework.
We won't be using Git Bugs on this assignment. If you need help on this homework, you should ask your lab TA or visit office hours for in person help. You're also welcome to use Piazza, but please read our Piazza guidelines before that.
