use adv_code_2024::*;
use anyhow::*;
use code_timing_macros::time_snippet;
use const_format::concatcp;
use itertools::Itertools;
use std::fs::File;
use std::io::{BufRead, BufReader};
use std::str::SplitWhitespace;

const DAY: &str = "02";
const INPUT_FILE: &str = concatcp!("input/", DAY, ".txt");

const TEST: &str = "\
7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9"; // TODO: Add the test input

fn main() -> Result<()> {
    start_day(DAY);

    //region Part 1
    println!("=== Part 1 ===");

    fn check_report(line: SplitWhitespace) -> bool {
        let report = line
            .tuple_windows()
            .map(|(first, second)| first.parse::<i32>().unwrap() - second.parse::<i32>().unwrap())
            .collect_vec();

        let is_safe_asc = report.iter().all(|col| (col >= &1) && (col <= &3));
        let is_safe_desc = report.iter().all(|col| (col <= &-1) && (col >= &-3));

        is_safe_desc || is_safe_asc
    }

    fn part1<R: BufRead>(reader: R) -> Result<usize> {
        let safe = reader
            .lines()
            .map(|line| check_report(line.unwrap().split_whitespace()))
            .filter(|&valid| valid)
            .count();

        Ok(safe)
    }

    assert_eq!(2, part1(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part1(input_file)?);
    println!("Result = {}", result);
    //endregion

    //region Part 2
    println!("\n=== Part 2 ===");

    fn part2<R: BufRead>(reader: R) -> Result<i32> {
        let mut safe = 0;
        for line in reader.lines() {
            let line = line?;
            let is_safe = check_report(line.split_whitespace());
            if is_safe {
                safe += 1;
                continue;
            }

            // handle second check
            let report_original = line.split_whitespace().collect_vec();
            for i in 0..report_original.len() {
                let mut report = report_original.to_vec();
                report.remove(i);
                let is_safe = check_report(report.join(" ").split_whitespace());
                if is_safe {
                    safe += 1;
                    break;
                }
            }
        }

        Ok(safe)
    }

    assert_eq!(4, part2(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part2(input_file)?);
    println!("Result = {}", result);
    // endregion

    Ok(())
}
