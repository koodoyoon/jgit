package com.example.jgit.Dto;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;

public class JgitUtil {
    private static String userId = "koodoyoon";
    private static String userPass = "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";
    private static String userName = "";
    private static String userEmail = "";
    private static String hash = "origin/master";
    private static String url = "https://github.com/koodoyoon/winterview.git";
    private static CredentialsProvider cp = new UsernamePasswordCredentialsProvider(userId, userPass);

    public static Git init(File dir) throws Exception {
        return Git.init().setDirectory(dir).call();
    }

    public static void remoteAdd(Git git) throws Exception {
        // add remote repo:
        RemoteAddCommand remoteAddCommand = git.remoteAdd();
        remoteAddCommand.setName("origin");
        remoteAddCommand.setUri(new URIish(url));
        // you can add more settings here if needed
        remoteAddCommand.call();
    }

    public static void push(Git git) throws Exception {
        // push to remote:
        PushCommand pushCommand = git.push();
        pushCommand.setCredentialsProvider(cp);
        pushCommand.setForce(true);
        // you can add more settings here if needed
        pushCommand.call();
    }

    public static void commit(Git git, String msg) throws Exception {
        // Now, we do the commit with a message
        git.commit()//
                .setAuthor(userName, userEmail)//
                .setMessage(msg)//
                .call();
    }

//    public static void lsRemote(Git git) throws Exception {
//        Collection<Ref> remoteRefs = git.lsRemote().setCredentialsProvider(cp).setRemote("origin").setTags(false).setHeads(true).call();
//        for (Ref ref : remoteRefs) {
//            System.out.println(ref.getName() + " -> " + ref.getObjectId().name());
//        }
//    }

    public static void checkOut(File dir) throws Exception {
        Git gitRepo = Git.cloneRepository().setURI(url) // remote 주소
                .setDirectory(dir) // 다운받을 로컬의 위치
                .setNoCheckout(true)//
                .setCredentialsProvider(cp) // 인증 정보
                .call();
        gitRepo.checkout().setStartPoint(hash) // origin/branch_name
                // .addPath("not thing") // 다운받을 대상 경로
                .call();

        gitRepo.getRepository().close();
    }

    public static Git open(File dir) throws Exception {
        Git git = null;
        try {
            git = Git.open(dir);
        } catch (RepositoryNotFoundException e) {
            git = JgitUtil.init(dir);
        }
        return git;
    }

    public static void main(String[] args) {
        String localPath = "/path/to/local/repository";
        String remotePath = "https://github.com/koodoyoon/winterview.git";
        String username = "koodoyoon";
        String password = "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";

        try (Repository repository = openRepository(localPath)) {
            Git git = new Git(repository);

            // 작업할 파일을 추가하고 커밋합니다.
            git.add().addFilepattern(".").call();
            git.commit().setMessage("Commit test").call();

            // 원격 저장소에 푸시합니다.
            CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(username, password);
            PushCommand pushCommand = git.push().setCredentialsProvider(credentialsProvider);
            pushCommand.setRemote(remotePath);
            Iterable<PushResult> pushResults = pushCommand.call();

            for (PushResult pushResult : pushResults) {
                // 푸시 결과를 처리합니다.
                System.out.println(pushResult.getMessages());
            }
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }
    private static Repository openRepository(String path) throws IOException {
        File repositoryDirectory = new File(path);
        return FileRepositoryBuilder.create(repositoryDirectory);
    }
}

