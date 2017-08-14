
void checkout(String repo_url, String branch){
  git repo: repo_url
  sh """/bin/bash -xe
    git checkout $branch
  """
}

void tag(String version, String ref){
  print "Tagging ${version} at ${ref}"
  sshagent (credentials:['rpc-jenkins-svc-github-ssh-key']){
    sh """/bin/bash -xe
      git tag -a -m \"$version\" $version $ref
      # TODO: CREDS
      git push --tags origin
    """
  }
}

//TODO: Create stabalisation branch if it doesn't exist
void update_rc_branch(String repo, String mainline, String stabalisation){
  print "Resetting ${stabalisation} to head of ${mainline}"
  sshagent (credentials:['rpc-jenkins-svc-github-ssh-key']){
    sh """/bin/bash -xe
      git fetch -a
      git checkout ${stabalisation}
      git reset --hard origin/${mainline}
      # TODO: CREDS
      git push -f git@github.com:rcbops/${repo}
    """
  }
}

return this
