// build flow dsl for running parallel jobs

def job_num = 100
parallel ((1..job_num).collect { index ->
  { -> build('run-tests-'+index, NUM_JOBS:job_num) }
})