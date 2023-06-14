package com.example.jgit;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.assertj.core.api.Assertions;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@SpringBootTest
class JgitApplicationTests {

    @Test
    @DisplayName("git init test")
    void gitInitTest() throws GitAPIException, IOException {
        //create git folder
        File gitDir = new File("C:\\Users\\entropy\\gitTest");
        if(gitDir.exists()){
            FileUtils.deleteDirectory(gitDir);
        }

        if(gitDir.mkdirs()){
            System.out.println("dir create success");
        }
        //init
        Git git = Git.init().setDirectory(gitDir).call();
        Assertions.assertThat(git).isNotNull();
        git.close();
    }

//    @Test
//    @DisplayName("git-hub clone test")
//    void gitHubCloneTest() throws GitAPIException, IOException {
//        //create git folder
//        File gitDir = new File("C:\\Users\\entropy\\gitTest");
//        if(gitDir.exists()){
//            FileUtils.deleteDirectory(gitDir);
//        }
//        System.out.println(gitDir);
//        if(gitDir.mkdirs()){
//            System.out.println("dir create success");
//        }
//
//        //set username, access token
//        CredentialsProvider credentialsProvider
//                = new UsernamePasswordCredentialsProvider(
//                "koodoyoon"
//                , "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"); //access token
//        System.out.println(credentialsProvider);
//        //clone
//        Git git = Git.cloneRepository()
//                .setURI("https://github.com/koodoyoon/winterview.git")
//                .setCredentialsProvider(credentialsProvider)
//                .setDirectory(gitDir)
//                .call();
//        System.out.println(git);
//        Assertions.assertThat(git).isNotNull();
//        git.close();
//    }

    @Test
    @DisplayName("git add test")
    void gitAddTest() throws GitAPIException, IOException {
        //git repo path
        String dirPath = "C:\\Users\\entropy\\gitTest";
        File gitDir = new File(dirPath);

        String fileName = UUID.randomUUID().toString();

        String contentToWrite = "Hello File!";
        String path = dirPath+"\\"+fileName+".txt";
        Files.write(Paths.get(path), contentToWrite.getBytes());

        //add
        Git git = Git.open(gitDir);
        Assertions.assertThat(git).isNotNull();

        AddCommand add = git.add();
        add.addFilepattern(fileName+".txt").call();

        git.close();
    }

    @Test
    @DisplayName("git commit test")
    void gitCommitTest() throws IOException, GitAPIException {
        //git repo path
        String dirPath = "C:\\Users\\entropy\\gitTest";
        File gitDir = new File(dirPath);

        //commit
        Git git = Git.open(gitDir);
        git.commit().setMessage("JGIT commit test").call();
        Assertions.assertThat(git).isNotNull();

        git.close();
    }

    @Test
    @DisplayName("git push test")
    void gitPushTest() throws IOException, GitAPIException {
        //git repo path
        String dirPath = "C:\\Users\\entropy\\gitTest";
        File gitDir = new File(dirPath);

        //set username, access token
        CredentialsProvider credentialsProvider
                = new UsernamePasswordCredentialsProvider(
                "koodoyoon"
                , "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"); //access token

        //push
        Git git = Git.open(gitDir);
        git.push()
                .setCredentialsProvider(credentialsProvider)
                .setRemote("origin")
                .setRefSpecs(new RefSpec("main"))
                .call();
        Assertions.assertThat(git).isNotNull();

        git.close();
    }
}
