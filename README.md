# Automated fixing of broken links for XWiki CMS
The job is to create a Java application that calls linkcheker (if necessary), analyze its journal and repairs content of the XWiki content management system of the site by opening, fixing content and storing the necessary site documents.

# Main features of the project
1) Web interface for user friendly input setting configuration
2) Launch LinkCheker to view the broken links
3) Launch LinkFixer to automatically fix broken links found by LinkCheker
4) User guide that explains the usage/setup of the project

# Installation (on Linux)
1) Install XWiki
2) Install LinkChecker
3) Install Curl
4) Import LinkFixer.WebHome.xar in the XWiki
5) Ensure that there are necessary rights to change the links
6) Use [your_site]/xwiki/bin/view/LinkFixer/GUI for fixing broken links
