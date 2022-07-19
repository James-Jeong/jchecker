Name: ucheck
Version: 1.0.0
Release: 1
Summary: ucheck
License: 2022, JAMESJ
Group: ucheck
autoprov: yes
autoreq: no
BuildRoot: /Users/jamesj/GIT_PROJECTS/UANGEL/ucheck/ucheck/target/rpm/ucheck/buildroot

%description
ucheck

%files
%defattr(644,ucheck,ucheck,755)
%dir  /home/ucheck/ucheck/
 /home/ucheck/ucheck/lib/
%attr(755,ucheck,ucheck) /home/ucheck/ucheck/bin//run.sh
%config(noreplace)  /home/ucheck/ucheck/config/
%dir  /home/ucheck/ucheck/logs/
