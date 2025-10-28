# StudentsPortfolioPro (Photos version)
This project adds photo-picking support to the Students Portfolio app.

IMPORTANT: If you uploaded a ZIP file to your GitHub repo earlier, DO NOT upload that ZIP file itself.
Instead you must upload the contents of this project (the files and folders) to the root of your repository.

How to put this into your GitHub repo (web UI):
1. Download this ZIP and extract it on your computer.
2. Go to your GitHub repository page.
3. Delete the previously uploaded StudentsPortfolioPro.zip file (if you uploaded it).
4. Click Add file → Upload files.
5. Drag the *contents* of the extracted folder (not the outer folder itself) into GitHub's upload area. This should place files like `settings.gradle.kts`, `app/`, `.github/` etc at the root of the repo.
6. Commit changes.
7. Go to Actions → Build Android APK → Run workflow → Wait.
8. Download the APK under Artifacts when finished.

If the build fails with "gradlew not found", open Android Studio locally and generate a Gradle wrapper (or ask me and I will provide follow-up steps).
